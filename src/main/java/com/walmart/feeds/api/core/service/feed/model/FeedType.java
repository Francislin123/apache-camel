package com.walmart.feeds.api.core.service.feed.model;

import lombok.Data;

@Data
public class FeedType {

    private FeedGenerationStrategy strategy;
    private FeedNotificationType notification;

}
