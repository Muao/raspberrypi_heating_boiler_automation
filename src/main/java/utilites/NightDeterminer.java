package utilites;

import java.time.LocalTime;

public class NightDeterminer {
    public static boolean isNight(LocalTime now){
        final LocalTime midnight = LocalTime.of(0, 0, 0);
        final LocalTime endOfNight = LocalTime.of(7, 0, 0);
        return now.isAfter(midnight) && now.isBefore(endOfNight);
    }
}
