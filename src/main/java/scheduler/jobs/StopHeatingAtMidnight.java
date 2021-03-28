package scheduler.jobs;

import DAO.repository.HeatingControllerLogRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import relay.RelayController;
import temperature_controller.HeatingController;

public class StopHeatingAtMidnight implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final RelayController controller = RelayController.getInstance();
        controller.stopFirstFloorHeating();
        controller.stopSecondFloorHeating();
        HeatingController.setNIGHT_MODE(true);
        HeatingControllerLogRepository.save("Stop Heating At Midnight", 0d);
    }
}
