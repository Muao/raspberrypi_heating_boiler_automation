package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import scheduler.jobs.StopHeatingAtMidnight;
import scheduler.jobs.StartHeatingAtMorning;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerListener {

    public static void init() throws SchedulerException {
        final SchedulerFactory sf = new StdSchedulerFactory();
        final Scheduler scheduler = sf.getScheduler();

        final JobDetail midnightStopJob = newJob(StopHeatingAtMidnight.class)
                .withIdentity("midnightStopJob", "group1")
                .build();

        final CronTrigger midnightStopTrigger = newTrigger()
                .withIdentity("stop at midnight", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("1 0 * * * ?"))
                .build();

        final JobDetail morningStartJob = newJob(StartHeatingAtMorning.class)
                .withIdentity("morningStartJob", "group1")
                .build();

        final CronTrigger morningStartTrigger = newTrigger()
                .withIdentity("start at morning", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 6 * * * ?"))
                .build();

        scheduler.scheduleJob(morningStartJob, morningStartTrigger);
        scheduler.scheduleJob(midnightStopJob, midnightStopTrigger);
        scheduler.start();
    }
}
