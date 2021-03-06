package telegram_bot;


import com.pi4j.io.gpio.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import statistic.Statistic;
import utilites.PReader;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger("TelegramBot");
    final static int RECONNECT_PAUSE = 10000;
    final GpioPinDigitalOutput op = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_29);

    @Getter
    String botUsername = PReader.read("TELEGRAM_BOT_NAME");
    @Getter
    String botToken = PReader.read("TELEGRAM_BOT_TOKEN");
    final int allowUserId1 = Integer.parseInt(PReader.read("ALLOW_USER_ID#1"));
    final int allowUserId2 = Integer.parseInt(PReader.read("ALLOW_USER_ID#2"));

    @Override
    public void onUpdateReceived(Update update) {
        final String chatId = update.getMessage().getChatId().toString();
        final User user = update.getMessage().getFrom();
        final SendMessage message = new SendMessage();
        message.setChatId(chatId);
        System.out.println(user.getUserName() + " " + user.getId());
        if (user.getId() == allowUserId1 || user.getId() == allowUserId2) {
            final String text = update.getMessage().getText();

            switch (text) {
                case "on": {
                    op.setState(true);
                    break;
                }
                case "off": {
                    op.setState(false);
                    break;
                }
                case "/stat": {
                message.setText(Statistic.get());
                break;
                }
                default: {
                    message.setText("unknown command: " + text);
                }
            }
            if (message.getText() == null) {
                final String state = op.getState().toString().equals("HIGH") ? "on" : "off";
                message.setText("led is " + state);
            }

        } else {
            message.setText("unknown user");
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void botConnect() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            System.out.println("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            System.out.println("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
