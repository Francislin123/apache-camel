package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;

import java.util.List;

public interface FeedService {

    FeedEntity createFeed(FeedEntity feedEntity);

    List<FeedEntity> fetchActiveByPartner(String partnerReference, Boolean active);

    void changeFeedStatus(String partnerSlug, String slug, Boolean active);

    void updateFeed(FeedEntity feedEntity);

    FeedEntity fetchByPartner(String feedSlug, String partnerSlug);

    void hasConflict(String slug);

    TermsBlacklistEntity findTermBlacklistBySlug(String slug);

    void validateFeed(String partnerSlug, String feedSlug);
}
