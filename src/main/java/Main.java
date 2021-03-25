import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import relay.RelayController;
import scheduler.SchedulerListener;
import telegram_bot.TelegramBot;
import utilites.ComPortReader;
import utilites.SoundUtils;

import java.io.IOException;

public class Main {
    private static final Logger log = LogManager.getLogger("Main");
    public static void main(String[] args) {

        SoundUtils.tone(800, 1000, 1);
        log.info("App starting..");

        final TelegramBot telegramBot = TelegramBot.getInstance();

        try {
            ComPortReader.read();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            SchedulerListener.init();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        final RelayController relayController = RelayController.getInstance();
        relayController.startFirstFlourHeating();
        relayController.startSecondFlourHeating();
    }
}

