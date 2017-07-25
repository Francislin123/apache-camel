package com.walmart.feeds.api.resources.feed.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FeedNotificationData {

    @NotNull
    private String method;

    @NotNull
    private String format;

    private String url;

}
