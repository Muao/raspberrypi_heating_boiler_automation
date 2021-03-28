package utilites;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class NightDeterminerTest {

    @Test
    public void isNightFalse() {
        assertFalse(NightDeterminer.isNight(LocalTime.of(7,10,0)));
    }

    @Test
    public void isNightTrue() {
        assertTrue(NightDeterminer.isNight(LocalTime.of(0,10,0)));
    }
}
