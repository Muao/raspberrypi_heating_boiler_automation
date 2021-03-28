package utilites;

import DAO.entities.ComPortDataEntity;
import DAO.repository.HeatingControllerLogRepository;

public class AlarmSystem {
    private static int errorsAcc = 0;

    public static void checkSensors(ComPortDataEntity comportData){
        if (comportData.getTempPort1() == -127 || comportData.getTempPort2() == -127 ||
                comportData.getTempPort1() == 0 || comportData.getTempPort2() == 0){
            errorsAcc ++;
        }
        if (errorsAcc > 10){
            HeatingControllerLogRepository.save("reboot raspberry pi on temperature sensors errors", comportData.getTempPort1());
            RaspberryPi.reboot();
        }
    }
}
