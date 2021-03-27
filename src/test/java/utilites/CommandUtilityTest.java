package utilites;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandUtilityTest {

    @Test
    public void isNewMode() {
        final boolean actual = CommandUtility.isNewMode("/newMode\"night\"");
        assertTrue(actual);
    }

    @Test
    public void getNewModeName() {

        final String actual = CommandUtility.getNewModeName("/newMode\"night\"");
        assertEquals("night", actual);
    }
}
