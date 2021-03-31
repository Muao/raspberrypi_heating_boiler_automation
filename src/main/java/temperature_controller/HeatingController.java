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
    private static boolean STOPPED_1ST_FLOOR;
    @Getter
    @Setter
    private static boolean STOPPED_2ND_FLOOR;
    @Getter
    @Setter
    private static boolean GLOBAL_STOPPED;

    public static void control(ComPortDataEntity data) {
        if (!NIGHT_MODE) {
            //first floor
            if (data.getTempPort1() >= 35 && !STOPPED_1ST_FLOOR) {
                relayController.stopFirstFloorHeating();
                setSTOPPED_1ST_FLOOR(true);
                HeatingControllerLogRepository.save("STOPPED 1st floor", data.getTempPort1());
            } else if (data.getTempPort1() <= 25 && STOPPED_1ST_FLOOR && !GLOBAL_STOPPED) {
                relayController.startFirstFloorHeating();
                setSTOPPED_1ST_FLOOR(false);
                HeatingControllerLogRepository.save("started 1st floor", data.getTempPort1());
            }

            //second floor
            if (data.getTempPort2() >= 35 && !STOPPED_2ND_FLOOR) {
                relayController.stopSecondFloorHeating();
                setSTOPPED_2ND_FLOOR(true);
                HeatingControllerLogRepository.save("STOPPED 2nd floor", data.getTempPort2());
            } else if (data.getTempPort2() <= 25 && STOPPED_2ND_FLOOR && !GLOBAL_STOPPED) {
                relayController.startSecondFloorHeating();
                setSTOPPED_2ND_FLOOR(false);
                HeatingControllerLogRepository.save("started 2nd floor", data.getTempPort2());
            }
        }
    }
}
