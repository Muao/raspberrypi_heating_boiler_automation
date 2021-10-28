import DAO.entities.ChatUserEntity;
import DAO.repository.ChatUserRepository;
import DAO.repository.HeatingControllerLogRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import scheduler.SchedulerListener;
import telegram_bot.TelegramBot;
import temperature_controller.HeatingController;
import utilites.ComPortReader;
import utilites.NightDeterminer;

import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//needs only register and start bot
        final TelegramBot telegramBot = TelegramBot.getInstance();

        new Thread(new ComPortReader()).start();
        new Thread(new SchedulerListener()).start();

        HeatingController.setNightMode(NightDeterminer.isNight(LocalTime.now()));
        HeatingController.setGLOBAL_STOPPED(false);
        final String startMessage = "Start application";
        HeatingControllerLogRepository.save(startMessage);

        final String baseUrl = telegramBot.getBaseUrl();
        System.out.println("------------------------------------>" + baseUrl);
        final List<ChatUserEntity> all = ChatUserRepository.INSTANCE.getAll();
        for (ChatUserEntity user : all) {
            try {
                telegramBot.execute(new SendMessage(user.getChatId(), startMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

