package com.walmart.feeds.api.core.repository.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartnerTaxonomyHistoryRepository extends JpaRepository<PartnerTaxonomyHistory, UUID> {
}
