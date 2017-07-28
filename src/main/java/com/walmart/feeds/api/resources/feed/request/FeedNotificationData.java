package com.walmart.feeds.api.resources.feed.request;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Data
public class FeedNotificationData {

    @NotNull
    private String method;

    @NotNull
    private String format;

    @URL
    private String url;

}
