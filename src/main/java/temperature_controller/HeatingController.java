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
    private static boolean FIRST_FLOOR_STOPPED = true;
    private static boolean SECOND_FLOOR_STOPPED = true;

    public static void control(ComPortDataEntity data) {
        if (!NIGHT_MODE) {
            //first floor
            if (data.getTempPort1() >= 30 && !FIRST_FLOOR_STOPPED) {
                relayController.stopFirstFloorHeating();
                HeatingControllerLogRepository.save("STOPPED 1st floor", data.getTempPort1());
                FIRST_FLOOR_STOPPED = true;
            } else if (data.getTempPort1() <= 23 && !GLOBAL_STOPPED && FIRST_FLOOR_STOPPED) {
                relayController.startFirstFloorHeating();
                HeatingControllerLogRepository.save("started 1st floor", data.getTempPort1());
                FIRST_FLOOR_STOPPED = false;
            }

            //second floor
            if (data.getTempPort2() >= 30 && !SECOND_FLOOR_STOPPED) {
                relayController.stopSecondFloorHeating();
                HeatingControllerLogRepository.save("STOPPED 2nd floor", data.getTempPort2());
                SECOND_FLOOR_STOPPED = true;
            } else if (data.getTempPort2() <= 23 && !GLOBAL_STOPPED && SECOND_FLOOR_STOPPED) {
                relayController.startSecondFloorHeating();
                HeatingControllerLogRepository.save("started 2nd floor", data.getTempPort2());
                SECOND_FLOOR_STOPPED = false;
            }
        }
    }
}
