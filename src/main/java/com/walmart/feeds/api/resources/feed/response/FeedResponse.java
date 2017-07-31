package com.walmart.feeds.api.resources.feed.response;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.UTM;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class FeedResponse {


    private String reference;

    private String name;

    private PartnerResponse partner;

    private FeedType feedType;

   private FeedNotificationData notification;

    private List<UTM> utms;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private boolean active;
}
