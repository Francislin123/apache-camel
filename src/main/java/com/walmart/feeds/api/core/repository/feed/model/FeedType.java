package com.walmart.feeds.api.core.repository.feed.model;

import lombok.Data;

@Data
public class FeedType {

    private FeedGenerationStrategy strategy;
    private FeedNotificationType notification;

}
