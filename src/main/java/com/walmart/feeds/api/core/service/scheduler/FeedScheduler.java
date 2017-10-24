package com.walmart.feeds.api.core.service.scheduler;

public interface FeedScheduler {

    void createFeedScheduler(String name, String group, String interval);

    void deleteJob(String name, String group);
}
