import DAO.repository.HeatingControllerLogRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scheduler.SchedulerListener;
import telegram_bot.TelegramBot;
import temperature_controller.HeatingController;
import utilites.ComPortReader;
import utilites.NightDeterminer;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
//needs only register and start bot
        final TelegramBot telegramBot = TelegramBot.getInstance();

        new Thread(new ComPortReader()).start();
        new Thread(new SchedulerListener()).start();

        HeatingController.setNightMode(NightDeterminer.isNight(LocalTime.now()));
        HeatingController.setGLOBAL_STOPPED(false);

        HeatingControllerLogRepository.save("Start application");
    }
}

