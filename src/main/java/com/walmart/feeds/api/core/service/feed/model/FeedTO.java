package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.UTM;
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

    public FeedEntity toEntity(){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, FeedEntity.class);
    }

}
