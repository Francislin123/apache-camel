package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.feed.request.UTM;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Data
public class FeedTO {

    private String reference;
    private String name;
    private FeedNotificationDataTO notificationData;
    private FeedType type;
    private List<UTMTO> utms;
    private boolean active;
    private PartnerTO partner;
    private LocalDateTime creationDate;

}
