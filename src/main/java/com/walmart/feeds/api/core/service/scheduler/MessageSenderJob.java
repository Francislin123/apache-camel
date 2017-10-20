package com.walmart.feeds.api.core.service.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MessageSenderJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Job Triggered by -> keyName " + context.getTrigger().getJobKey().getName()
                + " and keyGroup: " + context.getTrigger().getJobKey().getGroup());
    }
}
