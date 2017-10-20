package com.walmart.feeds.api.core.service.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class FeedSchedulerImpl implements FeedScheduler{

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void createFeedScheduler(String name, String group) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(MessageSenderJob.class)
                .withIdentity(name, group).build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(15).repeatForever()).build();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

    }
}
