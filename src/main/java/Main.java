import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram_bot.TelegramBot;
import utilites.ComPortReader;
import utilites.SoundUtils;

import java.io.IOException;

public class Main {
    private static final Logger log = LogManager.getLogger("Main");
    public static void main(String[] args) {

        SoundUtils.tone(800, 1000, 1);
        log.info("App starting..");

        final TelegramBot telegramBot = new TelegramBot();

        try {
            telegramBot.botConnect();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try {
            ComPortReader.read();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

