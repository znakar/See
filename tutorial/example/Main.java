package tutorial.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import src.main.java.tutorial.example.Bot;


public class Main {
    public static void main(String[] args) throws TelegramApiException { // throw в Java используется для создания и выброса исключения (exception) в том месте, где произошла ошибка или возникла проблема, которую нужно обработать.
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }
}
