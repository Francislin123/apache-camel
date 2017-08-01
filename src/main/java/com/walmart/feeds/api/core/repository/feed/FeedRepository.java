package com.walmart.feeds.api.core.repository.feed;

import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
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
public interface FeedRepository extends JpaRepository<Feed, String> {

    Optional<Feed> findByReference(String reference);

    Optional<List<Feed>> findByActiveAndPartner(Boolean active, Partner partner);

    @Modifying
    @Transactional
    @Query("UPDATE Feed f SET f.active = ?2 WHERE f = ?1")
    void changeFeedStatus(Feed feedEntity, boolean active);


    Optional<List<Feed>> findByPartner(Partner partner);
}
