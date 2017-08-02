package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */

public interface FeedService {

    FeedEntity createFeed(FeedEntity feedEntity) throws NotFoundException;

    List<FeedEntity> fetchByPartner(String partnerReference, Boolean active) throws NotFoundException;

    void changeFeedStatus(String partnerSlug, String slug, Boolean active) throws NotFoundException;

    void updateFeed(FeedEntity feedEntity) throws NotFoundException;
}
