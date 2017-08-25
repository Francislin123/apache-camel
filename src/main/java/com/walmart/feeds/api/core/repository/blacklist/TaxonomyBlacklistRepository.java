package com.walmart.feeds.api.core.repository.blacklist;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxonomyBlacklistRepository extends JpaRepository<TaxonomyBlacklistEntity, UUID> {
}
