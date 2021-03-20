package utilites;


import java.time.LocalDateTime;

public class CalendarUtility {

    public static LocalDateTime[] previousNight(){
        final LocalDateTime now = LocalDateTime.now();
        if(now.getHour() > 7){
            return getBordersOfNight(now);
        }
        final LocalDateTime yesterday = now.minusDays(1);
        return getBordersOfNight(yesterday);
    }

    private static LocalDateTime[] getBordersOfNight(LocalDateTime localDateTime){
        final LocalDateTime startOfNight =
                LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0, 0);
        final LocalDateTime endOfNight = startOfNight.plusHours(7);
        return new LocalDateTime[]{startOfNight, endOfNight};
    }
}
