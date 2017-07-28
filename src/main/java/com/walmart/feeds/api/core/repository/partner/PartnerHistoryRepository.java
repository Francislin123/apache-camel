package com.walmart.feeds.api.core.repository.partner;

import com.walmart.feeds.api.core.repository.partner.model.PartnerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartnerHistoryRepository extends JpaRepository<PartnerHistory, UUID> {
}
