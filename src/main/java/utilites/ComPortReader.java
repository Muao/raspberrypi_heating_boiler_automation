package utilites;

import DAO.entities.ComPortDataEntity;
import com.pi4j.io.serial.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import temperature_controller.HeatingController;

import java.io.IOException;

public class ComPortReader implements Runnable{
    private static final Logger log = LogManager.getLogger("ComPortReader");
    @Getter
    static private ComPortDataEntity current;

    public static void read() throws IOException, InterruptedException {
        final Serial serial = SerialFactory.createInstance();
        int openingCounter = 0;
        while (!serial.isOpen()) {
            try {
        SerialConfig config = new SerialConfig();
        config.device(SerialPort.getDefaultPort())
                .baud(Baud._38400)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

                serial.open(config);
            } catch (IOException e) {
                openingCounter ++;
                e.printStackTrace();
                Thread.sleep(1000);
                log.error("try to open serial port. #" + openingCounter);
            }
        }

        final ComPortDataStorage dataStorage = new ComPortDataStorage();
        serial.addListener(event -> {
            try {
                final String stingFromComPort = event.getAsciiString();
                if (Character.isDigit(stingFromComPort.charAt(0))) {
                    final String[] fromComPort = (stingFromComPort.trim().split(","));
                    final ComPortDataEntity comportData = new ComPortDataEntity(fromComPort);
                    AlarmSystem.checkSensors(comportData);
                    HeatingController.control(comportData);

                    final ComPortDataEntity filtered = ComPortDataUtility.filterMeasurementErrors(comportData);
                    dataStorage.add(filtered);
                    current = filtered;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        try {
            read();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
