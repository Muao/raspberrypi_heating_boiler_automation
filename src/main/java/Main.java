import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.gpio.*;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

            System.out.println("it's working " );

//        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));

        GpioPinDigitalOutput op = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_29);
        for (int i = 1; i < 11; i++) {
//            op.high();
            op.setState(true);
            System.err.println("on" + i);
            Thread.sleep(3000);
            op.setState(false);
            System.err.println("off" + i);
            Thread.sleep(3000);
//            op.low();
        }

        final W1Master master = new W1Master();
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
            }
        }



        /*SerialPort serialPort = new SerialPort("COM1");
        try {
            //Открываем порт
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReader(serialPort), SerialPort.MASK_RXCHAR);
            //Отправляем запрос устройству
            serialPort.writeString("Get data");
        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
        }*/
    }
}
