package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationUrl;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@Validated
public class FeedRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @ValidFeedType
    private String type;

    @Valid
    @NotNull
    @ValidFeedNotificationUrl
    private FeedNotificationData notification;

    @NotEmpty
    private Map<String, String> utms;

    @NotNull
    private Boolean active;


}
