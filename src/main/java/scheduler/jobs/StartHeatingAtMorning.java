package scheduler.jobs;

import DAO.repository.HeatingControllerLogRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import relay.RelayController;
import temperature_controller.HeatingController;

public class StartHeatingAtMorning implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final RelayController controller = RelayController.getInstance();
        HeatingController.setNIGHT_MODE(false);
        controller.startFirstFloorHeating(true); //run 2d stage on morning
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.startSecondFloorHeating();
        HeatingControllerLogRepository.save("Start Heating At Morning", 0d);
    }
}
