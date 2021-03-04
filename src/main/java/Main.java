import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram_bot.TelegramBot;

public class Main {

    public static void main(String[] args){

            System.out.println("app started.." );

        final TelegramBot telegramBot = new TelegramBot();
        try {
            telegramBot.botConnect();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


        /*final W1Master master = new W1Master();
        final List<W1Device> w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
        for (W1Device device : w1Devices) {
            //this line is enought if you want to read the temperature
            System.out.println("Temperature: " + ((TemperatureSensor) device).getTemperature());
            //returns the temperature as double rounded to one decimal place after the point

            try {
                System.out.println("1-Wire ID: " + device.getId() +  " value: " + device.getValue());
                //returns the ID of the Sensor and the  full text of the virtual file
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

