package telegram_bot;


import DAO.repository.ComPortRepository;
import DAO.repository.CommandsLogRepository;
import DAO.repository.ModeRepository;
import DTO.ComPortDataMinMaxTemp;
import graphics.Graphics;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import relay.RelayController;
import reports.DailyReport;
import reports.LastNightReport;
import statistic.Statistic;
import temperature_controller.HeatingController;
import utilites.CalendarUtility;
import utilites.ComPortReader;
import utilites.CommandUtility;
import utilites.PReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class TelegramBot extends TelegramLongPollingBot {
    final static int RECONNECT_PAUSE = 10000;
    private static final Logger log = LogManager.getLogger("TelegramBot");
    private static TelegramBot instance;
    final int allowUserId1 = Integer.parseInt(PReader.read("ALLOW_USER_ID#1"));
    final int allowUserId2 = Integer.parseInt(PReader.read("ALLOW_USER_ID#2"));
    final CommandsLogRepository repository = new CommandsLogRepository();
    final ComPortRepository comPortRepository = new ComPortRepository();
    @Getter
    String botUsername = PReader.read("TELEGRAM_BOT_NAME");
    @Getter
    String botToken = PReader.read("TELEGRAM_BOT_TOKEN");

    private TelegramBot() {
        try {
            this.botConnect();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static TelegramBot getInstance() {
        if (instance == null) {
            instance = new TelegramBot();
        }
        return instance;
    }

    @Override
    public void onUpdateReceived(Update update) {
        final String chatId = update.getMessage().getChatId().toString();
        final User user = update.getMessage().getFrom();
        final SendMessage message = new SendMessage();
        message.setChatId(chatId);
        final String userName = user.getUserName();
        log.info(userName + " " + user.getId());
        if (user.getId() == allowUserId1 || user.getId() == allowUserId2) {
            final String text = update.getMessage().getText();
            log.info("command: " + text);

            switch (text) {
                case "/stop": {
                    final RelayController controller = RelayController.getInstance();
                    controller.stopFirstFloorHeating();
                    controller.stopSecondFloorHeating();
                    HeatingController.setGLOBAL_STOPPED(true);
                    message.setText(controller.firstFloorState() + controller.secondFloorState());
                    break;
                }

                case "/start": {
                    final RelayController controller = RelayController.getInstance();
                    controller.startFirstFloorHeating();
                    controller.startSecondFloorHeating();
                    HeatingController.setGLOBAL_STOPPED(false);
                    message.setText(controller.firstFloorState() + controller.secondFloorState());
                    break;
                }

                case "/stat": {
                    message.setText(Statistic.get());
                    repository.save(text, userName);
                    final RelayController controller = RelayController.getInstance();
                    message.setText(Statistic.get() + "\n-----------\n" + controller.firstFloorState() + controller.secondFloorState());
                    break;
                }
                case "/current": {
                    final String whetherReport = ComPortReader.getCurrent().toString();
                    final RelayController relayController = RelayController.getInstance();
                    final String firstFloreState = relayController.firstFloorState();
                    final String secondFloreState = relayController.secondFloorState();
                    message.setText(whetherReport + "\n------\n" + firstFloreState + secondFloreState);
                    repository.save(text, userName);
                    break;
                }
                case "/lastnight": {
                    final ComPortDataMinMaxTemp lastNightData = comPortRepository.getLastNightData();
                    message.setText(LastNightReport.get(lastNightData));
                    break;
                }

                case "/graph": {
                    final SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    BufferedImage myImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
                    final Integer[] xPoints = {0, 100, 200, 300};
                    java.util.List<Integer> list = new java.util.ArrayList<>(Arrays.asList(xPoints));

                    final Graphics2D graphImage = new Graphics(list).paintComponent(myImage.createGraphics());
                    graphImage.setBackground(Color.white);
                    graphImage.setColor(Color.RED);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(myImage, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                        InputStream is = new ByteArrayInputStream(os.toByteArray());
                        sendPhoto.setPhoto(new InputFile(is, "diagramm"));
                        execute(sendPhoto);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }

                    break;
                }

                default: {
                    if (CalendarUtility.dailyDateMatcher(text)) {
                        final String date = text.replace("/daily ", "");
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yy");
                        final LocalDate localDate = LocalDate.parse(date, formatter);
                        final ComPortDataMinMaxTemp average24HourData = comPortRepository.getAverage24HourData(localDate);
                        final String report = DailyReport.get(average24HourData);
                        message.setText(report);
                    } else if (CommandUtility.isNewMode(text)) {
                        final String[] split = text.split("\"");
                        final String modeName = split[1];
                        final String modeMessage = ModeRepository.createNewMode(modeName, userName);
                        message.setText(modeMessage);
                    } else {
                        message.setText("unknown command: " + text);
                    }
                }
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

    private void botConnect() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            log.info("Can't Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
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
