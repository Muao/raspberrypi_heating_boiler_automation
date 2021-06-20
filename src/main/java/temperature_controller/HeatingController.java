package temperature_controller;

import DAO.entities.ComPortDataEntity;
import DAO.repository.HeatingControllerLogRepository;
import lombok.Getter;
import lombok.Setter;
import relay.RelayController;

public class HeatingController {
    private static final RelayController relayController = RelayController.getInstance();
    private final static int MAX_TEMP = 30;
    private final static int MIN_TEMP = 16;
    @Getter
    @Setter
    private static boolean GLOBAL_STOPPED;
    @Getter
    private static boolean NIGHT_MODE;
    private static boolean FIRST_FLOOR_STOPPED = true;
    private static boolean SECOND_FLOOR_STOPPED = true;

    public static void control(ComPortDataEntity data) {
        final double firstFloorTemp = data.getTempPort1();
        final double secondFloorTemp = data.getTempPort2();
        final double outdoorTemp = data.getTempPort4();

        //first floor
        if (firstFloorTemp >= MAX_TEMP && !FIRST_FLOOR_STOPPED) {
            relayController.stopFirstFloorHeating();
            FIRST_FLOOR_STOPPED = true;
            HeatingControllerLogRepository.save("STOPPED 1st floor", firstFloorTemp);

        } else if (startCondition(firstFloorTemp) && FIRST_FLOOR_STOPPED) {
            relayController.startFirstFloorHeating(outdoorTemp, firstFloorTemp, false);
            FIRST_FLOOR_STOPPED = false;
            HeatingControllerLogRepository.save("started 1st floor", firstFloorTemp);
        }
        //second floor
        if (secondFloorTemp >= MAX_TEMP && !SECOND_FLOOR_STOPPED) {
            relayController.stopSecondFloorHeating();
            SECOND_FLOOR_STOPPED = true;
            HeatingControllerLogRepository.save("STOPPED 2nd floor", secondFloorTemp);

        } else if (startCondition(secondFloorTemp) && SECOND_FLOOR_STOPPED) {
            relayController.startSecondFloorHeating();
            HeatingControllerLogRepository.save("started 2nd floor", secondFloorTemp);
            SECOND_FLOOR_STOPPED = false;
        }
    }

    public static void setNightMode(boolean nightMode) {
        NIGHT_MODE = nightMode;
        HeatingControllerLogRepository.save("Set Night Mode = " + nightMode, 0d);
    }

    public static void setFirstFloorStopped(boolean state, String userName) {
        FIRST_FLOOR_STOPPED = state;
        HeatingControllerLogRepository.save(
                String.format("Manual " + getMessage(state) + " 1st floor by user %s", userName));
    }

    public static void setSecondFloorStopped(boolean state, String userName) {
        SECOND_FLOOR_STOPPED = state;
        HeatingControllerLogRepository.save(
                String.format("Manual " + getMessage(state) + " 2st floor by user %s", userName));
    }

    public static void manualStart(String userName) {
        FIRST_FLOOR_STOPPED = false;
        SECOND_FLOOR_STOPPED = false;
        GLOBAL_STOPPED = false;
        HeatingControllerLogRepository.save(String.format("Manual started all by user %s", userName));
    }

    public static void manualStop(String userName) {
        FIRST_FLOOR_STOPPED = true;
        SECOND_FLOOR_STOPPED = true;
        GLOBAL_STOPPED = true;
        HeatingControllerLogRepository.save(String.format("Manual STOPPED ALL by user %s", userName));
    }

    private static boolean startCondition(double temp) {
        // temp < 4 - avoid freeze heating system
        return (temp < MIN_TEMP && !GLOBAL_STOPPED && !NIGHT_MODE) || temp < 4;
    }

    private static String getMessage(boolean state) {
        return state ? "started" : "STOPPED";
    }


}
