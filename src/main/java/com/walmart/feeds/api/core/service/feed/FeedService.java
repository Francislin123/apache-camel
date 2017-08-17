package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */

public interface FeedService {

    FeedEntity createFeed(FeedEntity feedEntity);

    List<FeedEntity> fetchActiveByPartner(String partnerReference, Boolean active);

    void changeFeedStatus(String partnerSlug, String slug, Boolean active);

    void updateFeed(FeedEntity feedEntity);

    FeedEntity fetchByPartner(String feedSlug, String partnerSlug);

    void hasConflict(String slug) throws EntityAlreadyExistsException;
}
