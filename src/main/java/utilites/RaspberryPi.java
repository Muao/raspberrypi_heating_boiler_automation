package utilites;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RaspberryPi {
    private static final Logger log = LogManager.getLogger("RaspberryPi");

    public static void reboot(){
        try {
            Runtime r = Runtime.getRuntime();
            r.exec("sudo reboot now");
        } catch (NumberFormatException | IOException e) {
            log.info("Restart raspberry PI failed.");
        }
    }

}
