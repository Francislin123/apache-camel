package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Data
public class FeedTO {

    private String reference;
    private String name;
    private String partnerReference;
    private FeedNotificationDataTO notificationData;
    private FeedType type;
    private List<UTMTO> utms;
    private boolean active;

    public Feed toEntity() {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, Feed.class);
    }

}
