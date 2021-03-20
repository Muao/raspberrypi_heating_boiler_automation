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

    @Test
    public void dailyDateMatcher(){
        final boolean actual = CalendarUtility.dailyDateMatcher("/daily 30.02.21");
        {
            assertTrue(actual);
        }
    }

    @Test
    public void dailyDateMatcherFalseSpace(){
        final boolean actual = CalendarUtility.dailyDateMatcher("/daily30.02.21");
        {
            assertFalse(actual);
        }
    }

    @Test
    public void dailyDateMatcherTrueCapitalize(){
        final boolean actual = CalendarUtility.dailyDateMatcher("/Daily 30.02.21");
        {
            assertTrue(actual);
        }
    }

    @Test
    public void dailyDateMatcherFalseFullYear(){
        final boolean actual = CalendarUtility.dailyDateMatcher("/daily 30.02.2021");
        {
            assertFalse(actual);
        }
    }
}
