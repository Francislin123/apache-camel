package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationFormat;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class FeedNotificationData {

    @NotNull
    @ValidFeedNotificationMethod
    private String method;

    @NotNull
    @ValidFeedNotificationFormat
    private String format;

    @URL
    private String url;

    @Tolerate

    public FeedNotificationData() {
    }
}
