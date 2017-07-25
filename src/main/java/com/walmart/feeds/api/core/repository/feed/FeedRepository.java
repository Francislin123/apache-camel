package com.walmart.feeds.api.core.repository.feed;

import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, String> {

    public List<FeedEntity> findByPartnerId(String partnerId);
    public List<FeedEntity> findByActive(Boolean active);

}
