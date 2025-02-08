package src.main.java.tutorial.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 МБ

    @Override
    public String getBotUsername() {
        return "SeechearBot";  // Имя бота
    }

    @Override
    public String getBotToken() {
        return "BOT_TOKEN";  // Токен бота
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                sendInlineKeyboard(chatId);
            }
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            handleFileUpload(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }

    private void handleFileUpload(Message message) {
        Document document = message.getDocument();
        Long chatId = message.getChatId();

        Integer fileSize = Math.toIntExact(document.getFileSize());
        String fileName = document.getFileName();

        if (fileSize == null || fileSize > MAX_FILE_SIZE) {
            sendMessage(chatId, "Файл слишком большой. Максимальный размер 5 МБ.");
            return;
        }

        String fileId = document.getFileId();

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(new GetFile(fileId));
            String filePath = file.getFilePath();
            String downloadUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + filePath;

            saveFileToDatabase(chatId, fileName, fileSize, downloadUrl);
            downloadFile(downloadUrl, "downloads/" + fileName);

        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendMessage(chatId, "Ошибка при загрузке файла.");
        }
    }

    private void saveFileToDatabase(Long chatId, String fileName, int fileSize, String fileUrl) {
        String query = "INSERT INTO files (user_id, file_name, file_size, file_url) VALUES (?, ?, ?, ?)";

        try (Connection con = MySQL.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setLong(1, chatId); // Используем setLong для Long
            pstmt.setString(2, fileName);
            pstmt.setInt(3, fileSize); // Используем setInt для int
            pstmt.setString(4, fileUrl);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                sendMessage(chatId, "Файл успешно сохранён в базе данных.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sendMessage(chatId, "Ошибка при сохранении файла в базе данных.");
        }
    }

    private void downloadFile(String downloadUrl, String localPath) {
        try (InputStream in = new URL(downloadUrl).openStream()) {
            Files.copy(in, Paths.get(localPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "help":
                sendHelpMessage(chatId);
                break;
            case "save":
                sendMessage(chatId, "Отправьте файл для сохранения.");
                break;
            case "delete":
                deleteFile(chatId);
                break;
            default:
                sendUnknownCommandMessage(chatId);
        }
    }

    private void sendInlineKeyboard(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет! Этот бот представляет разный функционал. Что ты хочешь сделать?");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton("Получить помощь");
        button1.setCallbackData("help");

        InlineKeyboardButton button2 = new InlineKeyboardButton("Сохранить файл");
        button2.setCallbackData("save");

        InlineKeyboardButton button3 = new InlineKeyboardButton("Удалить файл");
        button3.setCallbackData("delete");

        List<List<InlineKeyboardButton>> keyboard = List.of(
                List.of(button1), List.of(button2), List.of(button3)
        );

        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendHelpMessage(Long chatId) {
        sendMessage(chatId, "Здесь вы можете получить помощь. Команды:\n/start - Начать работу\n/help - Получить помощь.");
    }

    private void deleteFile(Long chatId) {
        sendMessage(chatId, "Функция удаления файла в разработке.");
    }

    private void sendUnknownCommandMessage(Long chatId) {
        sendMessage(chatId, "Неизвестная команда.");
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
