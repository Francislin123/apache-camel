package com.walmart.feeds.api.core.repository.feed;

import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedHistoryRepository extends JpaRepository<FeedHistory, UUID> {
}
