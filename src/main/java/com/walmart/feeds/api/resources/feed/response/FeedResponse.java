package com.walmart.feeds.api.resources.feed.response;

import com.walmart.feeds.api.resources.feed.request.FeedGenerationStrategy;
import com.walmart.feeds.api.resources.feed.request.UTM;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class FeedResponse {

    private UUID id;

    private String reference;

    private String name;

    // FIXME: 24/07/17 Referenciar Objeto partner
    private String partnerId;

    private FeedGenerationStrategy strategy;

    private String notificationMethod;

    private String notificationFormat;

    private String notificationUrl;

    private List<UTM> utms;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private boolean active;
}
