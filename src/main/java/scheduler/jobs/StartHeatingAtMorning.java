package scheduler.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import relay.RelayController;
import temperature_controller.HeatingController;

public class StartHeatingAtMorning implements Job {
    private static final Logger log = LogManager.getLogger("SwitchOnHeatingAtMorning");
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final RelayController controller = RelayController.getInstance();
        controller.startFirstFlourHeating();
        controller.startSecondFlourHeating();
        HeatingController.setNIGHT_MODE(false);
        log.info("successfully started at morning");
    }
}
