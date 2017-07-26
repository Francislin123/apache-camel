package com.walmart.feeds.api.resources.feed.request;

import lombok.Data;
import org.junit.validator.ValidateWith;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class FeedNotificationData {

    @NotNull
    private String method;

    @NotNull
    private String format;

    private String url;

}
