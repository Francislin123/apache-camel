package com.walmart.feeds.api.core.service.scheduler;

import com.walmart.feeds.api.core.exceptions.SystemException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class FeedSchedulerImpl implements FeedScheduler{

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public static final String DEFAULT_CRON_INTERVAL = "0 0 0/1 * * ?";

    @Override
    public void createFeedScheduler(String name, String group, String interval) {

        try {
            JobDetail job = JobBuilder.newJob(MessageSenderJob.class)
                    .withIdentity(name, group).build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(CronScheduleBuilder.cronSchedule(interval)
                            .withMisfireHandlingInstructionFireAndProceed()).build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException ex){
            throw new SystemException("Quartz exception", ex);
        }

    }

    @Override
    public void deleteJob(String name, String group) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = new JobKey(name, group);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException ex){
            throw new SystemException("Quartz exception", ex);
        }
    }


}
