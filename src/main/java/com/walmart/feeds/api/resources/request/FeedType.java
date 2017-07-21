package com.walmart.feeds.api.resources.request;

import lombok.Data;

@Data
public class FeedType {

    private FeedGenerationStrategy strategy;
    private FeedNotificationType notification;

}
