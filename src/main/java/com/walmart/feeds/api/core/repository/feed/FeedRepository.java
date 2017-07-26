package com.walmart.feeds.api.core.repository.feed;

import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, String> {

    Optional<FeedEntity> findByReference(String reference);
    List<FeedEntity> findByPartnerId(String partnerId);
    List<FeedEntity> findByActive(Boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE FeedEntity f SET f.active = ?2 WHERE f = ?1")
    void changeFeedStatus(FeedEntity feedEntity , boolean active);
}
