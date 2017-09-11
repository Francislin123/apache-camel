package com.walmart.feeds.api.core.repository.blacklist;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxonomyBlacklistRepository extends JpaRepository<TaxonomyBlacklistEntity, UUID> {

    Optional<TaxonomyBlacklistEntity> findBySlug(String slug);

    @Query("SELECT t FROM TaxonomyBlacklistEntity t " +
            "join t.list m " +
            "where m.taxonomy = ?1")
    List<TaxonomyBlacklistEntity> findByTaxonomyPath(String taxonomyPath);
}
