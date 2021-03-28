import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import scheduler.SchedulerListener;
import telegram_bot.TelegramBot;
import temperature_controller.HeatingController;
import utilites.ComPortReader;
import utilites.NightDeterminer;
import utilites.SoundUtils;

import java.io.IOException;
import java.time.LocalTime;

public class Main {
    private static final Logger log = LogManager.getLogger("Main");

    public static void main(String[] args) {

        SoundUtils.tone(800, 1000, 1);
        log.info("App starting..");
//needs only register and start bot
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

        HeatingController.setNIGHT_MODE(NightDeterminer.isNight(LocalTime.now()));
    }
}

