package com.walmart.feeds.api.resources.feed.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
public class FeedResponse {

    private String slug;

    private String name;

    private PartnerResponse partner;

    private FeedType type;

    private String template;

    private FeedNotificationData notification;

    private String taxonomy;

    private String fieldMapping;

    private Map<String, String> utms;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

    private boolean active;

}
