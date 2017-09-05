package com.walmart.feeds.api.core.repository.taxonomy;

import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerTaxonomyRepository extends JpaRepository<PartnerTaxonomyEntity, UUID> {

    Optional<PartnerTaxonomyEntity> findBySlug(String slug);

    Optional<List<PartnerTaxonomyEntity>> findByPartner(PartnerEntity partner);

    Optional<PartnerTaxonomyEntity> findBySlugAndPartner(String slug, PartnerEntity partner);

    Optional<PartnerTaxonomyEntity> findBySlugAndPartnerAndStatus(String slug, PartnerEntity partner, ImportStatus status);

}
