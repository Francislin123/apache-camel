package com.walmart.feeds.api.core.service.scheduler;

import org.quartz.SchedulerException;

/**
 * Created by vn0y942 on 19/10/17.
 */
public interface FeedScheduler {

    public void createFeedScheduler(String name, String group) throws SchedulerException;
}
