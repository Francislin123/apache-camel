package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationUrl;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Data
public class FeedRequest {

    @NotEmpty
    @Size(min = 1, max = 20)
    private String reference;

    @NotEmpty
    @Size(min = 1, max = 50)
    private String name;

    @NotEmpty
    @ValidFeedType(message = "Invalid type for feed")
    private String type;

    @Valid
    @NotNull
    @ValidFeedNotificationUrl(message = "The field url is required if the type is 'api'")
    private FeedNotificationData notification;

    @NotEmpty
    private List<UTM> utms;


}
