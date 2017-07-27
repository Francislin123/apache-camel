package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FeedRequest {

    @NotNull
    @Size(min = 1, max = 20)
    private String reference;

    @NotNull
    private String name;

    @ValidFeedType
    private String type;

    @Valid
    private FeedNotificationData notification;
    private List<UTM> utms;


}
