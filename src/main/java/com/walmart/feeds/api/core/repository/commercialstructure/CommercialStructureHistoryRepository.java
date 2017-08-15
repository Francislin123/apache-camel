package com.walmart.feeds.api.core.repository.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommercialStructureHistoryRepository extends JpaRepository<CommercialStructureHistory, UUID> {
}
