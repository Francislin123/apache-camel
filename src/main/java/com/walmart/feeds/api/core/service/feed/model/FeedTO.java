package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Data
public class FeedTO {

    private String reference;
    private String name;
    private FeedNotificationDataTO notificationData;
    private FeedType type;
    private Map<String, String> utms;
    private boolean active;
    private PartnerTO partner;
    private LocalDateTime creationDate;

}
