package com.walmart.feeds.api.core.repository.blacklist;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxonomyBlacklistHistoryRepository extends JpaRepository<TaxonomyBlacklistHistory, UUID> {
}
