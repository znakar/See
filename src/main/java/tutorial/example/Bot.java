package src.main.java.tutorial.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;



import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "SeechearBot";  // Имя бота
    }

    @Override
    public String getBotToken() {
        return "8138869677:AAFdZ-uxIZbNin0QwesyrkHkcppvkMMqAJo";  // Токен бота
    }

    @Override
    public void onUpdateReceived(Update update) { // определение открытого статического метода, вызов которого не подразумевает возвращение результата и не требует создания экземпляра класса
        if (update.hasMessage() && update.getMessage().hasText()) { // проверяем есть ли сообщение
            Long chatId = update.getMessage().getChatId(); // Бот извлекает ID чата, откуда пришло сообщение
            String messageText = update.getMessage().getText(); // Бот извлекает текст сообщения, чтобы понять, что написал пользователь

            if (messageText.equals("/start")) {
                sendInlineKeyboard(update.getMessage().getChatId());
            }
        } else if (update.hasCallbackQuery()) {
            // Обрабатывает нажатие на inline кнопки
            handleCallback(update.getCallbackQuery());
        }
    }
    private void handleCallback(CallbackQuery callbackQuery) {
        Long ChatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "help":
                sendHelpMessage(ChatId);
                break;
            case "save":
                saveFile(ChatId);
                break;
            case "delete":
                deleteFile(ChatId);
                break;
            default:
                sendUnknownCommandMessaage(ChatId);
        }
    }


    private void sendInlineKeyboard(Long chatId) {
        // сообщение с вопросом
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет! Этот бот представляет разный функционал. Что ты хочешь сделать?");

        // создание inline кнопок
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Получить помощь");
        button1.setCallbackData("help");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Сохранить файл");
        button2.setCallbackData("save");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Удалить файл");
        button3.setCallbackData("delete");

        // Сбор кнопок
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(button1);


        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(button2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(button3);


        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);




        // Установка клавиатуры
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


// Cообщение помощи
private void sendHelpMessage(Long chatId) {
    sendMessage(chatId, "Здесь вы можете получить помощь. Команды:\n/start - Начать работу\n/write - Сохранить файл\n/remove - Удалить файл\n/help - Получить помощь.");
}

// Cохранение файла

private void saveFile(Long chatId) {
    sendMessage(chatId,"Функция сохранения файла в разработке.");
}

// Удаление файла

private void deleteFile(Long chatId) {
    sendMessage(chatId, "Функция удаления файла в разработке.");
}

private void sendUnknownCommandMessaage(Long chatId) {
    sendMessage(chatId, "Неизвестная команда.");
}

// Метод для отправки ссобщений

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
private void sendWelcomeMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет! Я твой новый бот. Чем могу помочь?");

    // Replay клавиатура
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true); // Подгоняет кнопки под экран
    replyKeyboardMarkup.setOneTimeKeyboard(true); // Скрывает клавиатуру после нажатия

    // Кнопка "Перезапустить бота"
    KeyboardRow row = new KeyboardRow();
    row.add(new KeyboardButton("Перезапустить бота"));

    // Добавление кнопку в клавиатуру
    List<KeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(row);
    replyKeyboardMarkup.setKeyboard(keyboard);

    message.setReplyMarkup(replyKeyboardMarkup); // Устанавливаем клавиатуру
    try {
        execute(message); // Отправляем сообщение с клавиатурой
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
}
}
