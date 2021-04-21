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
    private static int MAX_TEMP = 30;
    private static int MIN_TEMP = 23;

    private static String getMessage(boolean state){
        return state ? "started" : "STOPPED";
    }

    public static void setFirstFloorStopped(boolean state, String userName) {
        FIRST_FLOOR_STOPPED = state;
        HeatingControllerLogRepository.save(String.format("Manual " + getMessage(state)+ " 1st floor by user %s", userName));
    }

    public static void setSecondFloorStopped(boolean state, String userName) {
        SECOND_FLOOR_STOPPED = state;
        HeatingControllerLogRepository.save(String.format("Manual " + getMessage(state)+ " 2st floor by user %s", userName));
    }

    public static void manualStart(String userName){
        FIRST_FLOOR_STOPPED = false;
        SECOND_FLOOR_STOPPED = false;
        GLOBAL_STOPPED = false;
        HeatingControllerLogRepository.save(String.format("Manual started all by user %s", userName));
    }

    public static void manualStop(String userName){
        FIRST_FLOOR_STOPPED = true;
        SECOND_FLOOR_STOPPED = true;
        GLOBAL_STOPPED = true;
        HeatingControllerLogRepository.save(String.format("Manual STOPPED ALL by user %s", userName));
    }

    public static void control(ComPortDataEntity data) {
        final double firstFloorTemp = data.getTempPort1();
        //(firstFloorTemp < 4 && firstFloorTemp > -20) - avoid freeze heating system
        if (!NIGHT_MODE || (firstFloorTemp < 4 && firstFloorTemp > -20)) {
            //first floor

            final double outdoorTemp = data.getTempPort4();
            if (firstFloorTemp >= MAX_TEMP && !FIRST_FLOOR_STOPPED) {
                relayController.stopFirstFloorHeating();
                HeatingControllerLogRepository.save("STOPPED 1st floor", firstFloorTemp);
                FIRST_FLOOR_STOPPED = true;
            } else if (firstFloorTemp <= MIN_TEMP && !GLOBAL_STOPPED && FIRST_FLOOR_STOPPED) {
                relayController.startFirstFloorHeating(outdoorTemp, firstFloorTemp, false);
                HeatingControllerLogRepository.save("started 1st floor", firstFloorTemp);
                FIRST_FLOOR_STOPPED = false;
            }

            //second floor
            if (data.getTempPort2() >= MAX_TEMP && !SECOND_FLOOR_STOPPED) {
                relayController.stopSecondFloorHeating();
                HeatingControllerLogRepository.save("STOPPED 2nd floor", data.getTempPort2());
                SECOND_FLOOR_STOPPED = true;
            } else if (data.getTempPort2() <= MIN_TEMP && !GLOBAL_STOPPED && SECOND_FLOOR_STOPPED) {
                relayController.startSecondFloorHeating();
                HeatingControllerLogRepository.save("started 2nd floor", data.getTempPort2());
                SECOND_FLOOR_STOPPED = false;
            }
        }
    }


}
