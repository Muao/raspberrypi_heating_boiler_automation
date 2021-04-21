package utilites;

import org.junit.Test;

import static org.junit.Assert.*;

public class RelayControllerUtilityTest {

    @Test
    public void isSecondStageRunWinter() {
        final boolean actual = RelayControllerUtility.isSecondStageMustRun(-11.0, 11, false);
        assertTrue(actual);
    }

    @Test
    public void isSecondStageRunForce() {
        final boolean actual = RelayControllerUtility.isSecondStageMustRun(-1.0, 11, true);
        assertTrue(actual);
    }

    @Test
    public void isSecondStageRunAll() {
        final boolean actual = RelayControllerUtility.isSecondStageMustRun(-11.0, 11, true);
        assertTrue(actual);
    }

    @Test
    public void isSecondStageRunNoOne() {
        final boolean actual = RelayControllerUtility.isSecondStageMustRun(-1.0, 11, false);
        assertFalse(actual);
    }

    @Test
    public void isSecondStageRunWithoutTemp() {
        final boolean actual = RelayControllerUtility.isSecondStageMustRun(0d, 0d, false);
        assertFalse(actual);
    }

    @Test
    public void isSecondStageRunWithoutTempForce() {
        final boolean actual = RelayControllerUtility.isSecondStageMustRun(0d, 0d, true);
        assertTrue(actual);
    }
}
