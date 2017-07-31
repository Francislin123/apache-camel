package com.walmart.feeds.api.core.service.feed.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedNotificationDataTO {

    private String method;
    private String format;
    private String url;
}
