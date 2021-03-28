package temperature_controller;

import DAO.entities.ComPortDataEntity;
import lombok.Getter;
import lombok.Setter;
import relay.RelayController;

public class HeatingController {
    private static final RelayController relayController = RelayController.getInstance();
    @Getter
    @Setter
    private static boolean NIGHT_MODE;

    public static void control(ComPortDataEntity data) {
        if (!NIGHT_MODE) {
            //first floor
            if (data.getTempPort1() >= 35) {
                relayController.stopFirstFlourHeating();
            } else if (data.getTempPort1() <= 30) {
                relayController.startFirstFlourHeating();
            }
            //second floor
            if (data.getTempPort2() >= 35) {
                relayController.stopSecondFlourHeating();
            } else if (data.getTempPort2() <= 30) {
                relayController.startSecondFlourHeating();
            }

        }
    }
}
