package utilites;

import DAO.entities.ComPortDataEntity;
import com.pi4j.io.serial.*;
import lombok.Getter;
import temperature_controller.HeatingController;

import java.io.IOException;

public class ComPortReader {
    @Getter static private ComPortDataEntity current;

    public static void read() throws IOException, InterruptedException {
        final Serial serial = SerialFactory.createInstance();
        SerialConfig config = new SerialConfig();
        config.device(SerialPort.getDefaultPort())
                .baud(Baud._38400)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        serial.open(config);
        final ComPortDataStorage dataStorage = new ComPortDataStorage();
        serial.addListener(event -> {
            try {
                final String stingFromComPort = event.getAsciiString();
                if (Character.isDigit(stingFromComPort.charAt(0))) {
                    final String[] fromComPort = (stingFromComPort.split(","));
//                    AlarmSystem.checkSensors(fromComPort);
                    final ComPortDataEntity comportData = new ComPortDataEntity(fromComPort);

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
}
