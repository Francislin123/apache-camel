package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private boolean active;

    public FeedEntity toEntity(){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, FeedEntity.class);
    }

}
