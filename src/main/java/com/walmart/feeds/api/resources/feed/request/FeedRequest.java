package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyMapEntry;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationUrl;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.Transient;
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

    @Valid
    @NotEmptyMapEntry
    private Map<String, String> utms;

    @NotNull
    private Boolean active;


    // CERTO?
    // @Transient
    private String template;




}
