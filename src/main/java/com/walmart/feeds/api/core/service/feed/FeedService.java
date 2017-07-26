package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */

public interface FeedService {

    void createFeed(FeedTO feedTO);
    public List<FeedTO> fetchByActiveAndByPartnerId(FeedTO feedTo)throws NotFoundException;
    public List<FeedTO> fetchByPartner(FeedTO feedTO) throws NotFoundException;
}
