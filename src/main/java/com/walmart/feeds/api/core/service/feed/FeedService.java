package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */

public interface FeedService {

    void createFeed(FeedTO feedTO) throws NotFoundException;

    List<FeedTO> fetchByActiveAndByPartner(String partnerReference) throws NotFoundException;

    List<FeedTO> fetchByPartner(String partnerReference, Boolean active) throws NotFoundException;

    void changeFeedStatus(String reference, Boolean active) throws NotFoundException;

    void updateFeed(FeedTO feedTO) throws NotFoundException;
}
