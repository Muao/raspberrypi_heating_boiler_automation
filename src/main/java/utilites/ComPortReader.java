package utilites;

import com.pi4j.io.serial.*;

import java.io.IOException;

public class ComPortReader {

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
                if (!stingFromComPort.startsWith("#")) {
                    final String[] fromComPort = (stingFromComPort.split(","));
//                    AlarmSystem.checkSensors(fromComPort);
                    final ComportData comportData = new ComportData(fromComPort);
                    dataStorage.add(comportData);
                    System.out.println(comportData.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
