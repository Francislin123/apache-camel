package com.walmart.feeds.api.core.repository.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxonomyMappingRepository extends JpaRepository<TaxonomyMappingEntity, UUID> {

    @Query("SELECT tm.partnerPath FROM TaxonomyMappingEntity tm JOIN tm.partnerTaxonomy pt " +
            "WHERE pt.partner.slug = ?1 AND pt.slug = ?2 AND tm.walmartPath = ?3")
    String findMappingByPartner(String partnerSlug, String slug, String walmartPath);

}
