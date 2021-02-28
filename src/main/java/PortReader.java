import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class PortReader implements SerialPortEventListener {
    private final SerialPort serialPort;

    public PortReader(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR() && event.getEventValue() > 0){
            try {
                //Получаем ответ от устройства, обрабатываем данные и т.д.
                String data = serialPort.readString(event.getEventValue());
                System.out.println(data);
                //И снова отправляем запрос
                serialPort.writeString("Get data");
            }
            catch (SerialPortException ex) {
                ex.printStackTrace();
            }
        }
    }
}

