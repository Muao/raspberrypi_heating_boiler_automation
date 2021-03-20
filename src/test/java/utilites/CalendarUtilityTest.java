package utilites;

import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CalendarUtilityTest {

    @Test
    @Ignore
    public void previousNight() {
//        test for 18.03.2021 22:02
        final LocalDateTime[] actual = CalendarUtility.previousNight();
        LocalDateTime[] expected = new LocalDateTime[]{
                LocalDateTime.of(2021, 3, 20, 0, 0, 0),
                LocalDateTime.of(2021, 3, 20, 7, 0, 0)
        };
        assertArrayEquals(expected, actual);
    }
}
