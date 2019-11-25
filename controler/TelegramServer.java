package controler;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tutorvirtual.Execucao;

/**
 *
 * @author Luísa
 */
public class TelegramServer extends Thread{
    @Override
    public void run() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Execucao());
            
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    } 
}
