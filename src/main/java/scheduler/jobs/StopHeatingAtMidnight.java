package scheduler.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import relay.RelayController;

public class StopHeatingAtMidnight implements Job {
    private static final Logger log = LogManager.getLogger("SwitchOffHeatingAtMidnight");
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final RelayController controller = RelayController.getInstance();
        controller.stopFirstFlourHeating();
        controller.stopSecondFlourHeating();
        log.info("successfully stopped at midnight");
    }
}
