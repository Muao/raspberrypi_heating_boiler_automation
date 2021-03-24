package scheduler.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import relay.RelayController;

public class StartHeatingAtMorning implements Job {
    private static final Logger log = LogManager.getLogger("SwitchOnHeatingAtMorning");
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final RelayController controller = RelayController.getInstance();
        controller.startFirstFlourHeating();
        controller.startSecondFlourHeating();

        log.info("successfully started at morning");
    }
}
