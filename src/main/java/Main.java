import com.pi4j.io.gpio.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

            System.out.println("it's working " );

//        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));

        GpioPinDigitalOutput op = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_29);
        for (int i = 0; i < 20; i++) {
//            op.high();
            op.setState(true);
            System.out.println("on");
            Thread.sleep(3000);
            op.setState(false);
            Thread.sleep(3000);
//            op.low();
            System.out.println("off");
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
