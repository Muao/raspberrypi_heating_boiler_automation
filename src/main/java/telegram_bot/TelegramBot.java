package telegram_bot;


import DAO.repository.CommandsLogRepository;
import DAO.repository.ModeRepository;
import DAO.servises.ChatUserService;
import DAO.servises.ComPortDataService;
import DTO.ComPortDataMinMaxTemp;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import graphics.Graphics;
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
import utilites.*;

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
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LogManager.getLogger("TelegramBot");
    private static TelegramBot instance;
    private final int allowUserId1 = Integer.parseInt(PReader.read("ALLOW_USER_ID#1"));
    private final int allowUserId2 = Integer.parseInt(PReader.read("ALLOW_USER_ID#2"));
    private final CommandsLogRepository repository = new CommandsLogRepository();

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
        final String userName = user.getUserName();

        message.setChatId(chatId);
        if (user.getId() == allowUserId1 || user.getId() == allowUserId2) {
            final String text = update.getMessage().getText();

            switch (text) {
                case "/reboot": {
                    RaspberryPi.reboot();
                    message.setText("..rebooting");
                    break;
                }

                case"/shutdown": {
                    RaspberryPi.shutdown();
                    message.setText("..shutdown");
                    break;
                }

                case "/stop": {
                    final RelayController controller = RelayController.getInstance();
                    controller.stopFirstFloorHeating();
                    controller.stopSecondFloorHeating();
                    HeatingController.manualStop(userName);
                    message.setText(controller.firstFloorState() + controller.secondFloorState());
                    break;
                }

                case "/stop1": {
                    final RelayController relayController = RelayController.getInstance();
                    relayController.stopFirstFloorHeating();
                    HeatingController.setFirstFloorStopped(true, userName);
                    message.setText(relayController.firstFloorState());
                    break;
                }

                case "/stop2": {
                    final RelayController relayController = RelayController.getInstance();
                    relayController.stopSecondFloorHeating();
                    HeatingController.setSecondFloorStopped(true, userName);
                    message.setText(relayController.secondFloorState());
                    break;
                }

                case "/start": {
                    final RelayController relayController = RelayController.getInstance();
                    relayController.startFirstFloorHeating(false);
                    relayController.startSecondFloorHeating();
                    HeatingController.manualStart(userName);
                    message.setText(relayController.firstFloorState() + relayController.secondFloorState());
                    break;
                }

                case "/start1": {
                    final RelayController relayController = RelayController.getInstance();
                    relayController.startFirstFloorHeating(false);
                    HeatingController.setFirstFloorStopped(false, userName);
                    message.setText(relayController.firstFloorState());
                    break;
                }

                case "/start2": {
                    final RelayController relayController = RelayController.getInstance();
                    relayController.startSecondFloorHeating();
                    HeatingController.setSecondFloorStopped(false, userName);
                    message.setText(relayController.secondFloorState());
                    break;
                }

                case "/warmer" : {
                    final RelayController relayController = RelayController.getInstance();
                    relayController.startFirstFloorHeating(true);
                    HeatingController.setFirstFloorStopped(false, userName);
                    message.setText(relayController.firstFloorState());
                    break;
                }

                case "/stat": {
                    repository.save(text, userName);
                    final RelayController controller = RelayController.getInstance();
                    final W1Master w1Master = new W1Master();
                    final List<W1Device> devices = w1Master.getDevices();
                    StringBuilder devises = new StringBuilder(String.valueOf(devices.size()));
                    for (W1Device d : devices) {
                        devises.append(d.getName())
                                .append(" --> ")
                                .append(d.getId())
                                .append(" --> ")
                                .append(d.getFamilyId()).append("\n");
                    }
                    message.setText(Statistic.get() +
                            "\n-----------\n" +
                            controller.allRelayState() +
                            "\n-----------\n" +
                            devises
                    );
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
                    final ComPortDataMinMaxTemp lastNightData = ComPortDataService.getLastNightData();
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
                case "/daily": {
                    final String report = DailyReport.get(ComPortDataService.getAverage24HourData(LocalDate.now().minusDays(1)));
                    message.setText(report);
                    break;
                }
                case "/subscribe_sys": {
                    message.setText(ChatUserService.saveUser(user, chatId));
                    break;
                }

                default: {
                    if (CalendarUtility.dailyDateMatcher(text)) {
                        final String date = text.replace("/daily ", "");
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yy");
                        final LocalDate localDate = LocalDate.parse(date, formatter);
                        final ComPortDataMinMaxTemp average24HourData = ComPortDataService.getAverage24HourData(localDate);
                        final String report = DailyReport.get(average24HourData);
                        message.setText(report);
                    } else if (CommandUtility.isNewMode(text)) {
                        final String[] split = text.split("\"");
                        final String modeName = split[1];
                        final String modeMessage = ModeRepository.createNewMode(modeName, userName);
                        message.setText(modeMessage);
                    } else if(text.startsWith("/ss")){
                        final String[] s = text.split(" ");
                        final RelayController controller = RelayController.getInstance();
                        final PinState state = Integer.parseInt(s[2]) == 0 ? PinState.LOW : PinState.HIGH;

                        final String set = controller.set(s[1], state);
                        message.setText(s[1] + " --> " + state + "==" + set);
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
        final int RECONNECT_PAUSE = 10000;
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

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

    @Override
    public String getBotUsername() {
        return PReader.read("TELEGRAM_BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return PReader.read("TELEGRAM_BOT_TOKEN");
    }
}
