package scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import temperature_controller.HeatingController;

public class StopHeatingAtMidnight implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        HeatingController.setNightMode(true);
    }
}
