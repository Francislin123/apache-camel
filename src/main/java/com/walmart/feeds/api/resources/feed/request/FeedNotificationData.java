package com.walmart.feeds.api.resources.feed.request;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class FeedNotificationData {

    @NotNull
    private String method;

    @NotNull
    private String format;

    @URL
    private String url;

    @Tolerate

    public FeedNotificationData() {
    }
}
