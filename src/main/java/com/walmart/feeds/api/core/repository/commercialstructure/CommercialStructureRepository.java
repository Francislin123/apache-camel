package com.walmart.feeds.api.core.repository.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface CommercialStructureRepository extends JpaRepository<CommercialStructureEntity, UUID> {
    Optional findBySlug(String slug);
}
