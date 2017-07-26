package com.walmart.feeds.api.core.service.feed.model;

import lombok.Data;

@Data
public class FeedNotificationDataTO {

    private String method;
    private String format;
    private String url;
}
