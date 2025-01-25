package src.main.java.tutorial.example;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;


public class Commands {

    public static void registerCommands(Bot bot) { // public - Позволяет вызывать метод из любого другого класса
        //static - позволяет вызывать метод без создания экземпляра класса, нет необходимости писать Commands = commands = new Commands()
        //void - указывает, что метод ничего не возвращает, метод выполняет действия (регистрирует команды), но результат не передается в вызывающий код
        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(Arrays.asList(
                new BotCommand("/start", "Начать работу с ботом"),
                new BotCommand("/write","Сохранить файл"),
                new BotCommand("/remove","Удалить файл"),
                new BotCommand("/help","Получить помощь")
        ));
try {
    bot.execute(setMyCommands); // Передаем команды через объект бота
} catch (TelegramApiException e) {
    e.printStackTrace();
        }
    }

}
