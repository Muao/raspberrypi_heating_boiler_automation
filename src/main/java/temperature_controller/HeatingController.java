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
    private static boolean STOPPED;

    public static void control(ComPortDataEntity data) {
        if (!NIGHT_MODE && !STOPPED) {
            //first floor
            if (data.getTempPort1() >= 40) {
                //todo needs to implements STOPPED_1ST_FLOOR and STOPPED_2ND_FLOOR for avoid logic mismatch
                relayController.stopFirstFloorHeating();
                HeatingControllerLogRepository.save("stopped 1st floor", data.getTempPort1());
            } else if (data.getTempPort1() <= 30) {
                relayController.startFirstFloorHeating();
                HeatingControllerLogRepository.save("started 1st floor", data.getTempPort1());
            }
            //second floor
            if (data.getTempPort2() >= 40) {
                relayController.stopSecondFloorHeating();
                HeatingControllerLogRepository.save("stopped 2st floor", data.getTempPort2());
            } else if (data.getTempPort2() <= 30) {
                relayController.startSecondFloorHeating();
                HeatingControllerLogRepository.save("started 2nd floor", data.getTempPort2());
            }

        }
    }
}
