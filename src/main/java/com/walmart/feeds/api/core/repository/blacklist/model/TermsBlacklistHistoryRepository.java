package com.walmart.feeds.api.core.repository.blacklist.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TermsBlacklistHistoryRepository extends JpaRepository<TermsBlacklistHistory, UUID> {
}
