package utilites;

public class RelayControllerUtility {

    public static boolean isSecondStageMustRun(double outdoorTemp, double firstFloorTemp, boolean force){
        return outdoorTemp < -10 && firstFloorTemp < 12 || force;
    }
}
