package temperature_controller;

import DAO.entities.ComPortDataEntity;
import DAO.repository.HeatingControllerLogRepository;
import lombok.Getter;
import lombok.Setter;
import relay.RelayController;

public class HeatingController {
    private static final RelayController relayController = RelayController.getInstance();
    @Getter
    @Setter
    private static boolean NIGHT_MODE;
    @Getter
    @Setter
    private static boolean GLOBAL_STOPPED;

    public static void control(ComPortDataEntity data) {
        if (!NIGHT_MODE) {
            //first floor
            if (data.getTempPort1() >= 30) {
                relayController.stopFirstFloorHeating();
                HeatingControllerLogRepository.save("STOPPED 1st floor", data.getTempPort1());
            } else if (data.getTempPort1() <= 23 && !GLOBAL_STOPPED) {
                relayController.startFirstFloorHeating();
                HeatingControllerLogRepository.save("started 1st floor", data.getTempPort1());
            }

            //second floor
            if (data.getTempPort2() >= 30) {
                relayController.stopSecondFloorHeating();
                HeatingControllerLogRepository.save("STOPPED 2nd floor", data.getTempPort2());
            } else if (data.getTempPort2() <= 23 && !GLOBAL_STOPPED) {
                relayController.startSecondFloorHeating();
                HeatingControllerLogRepository.save("started 2nd floor", data.getTempPort2());
            }
        }
    }
}
