package com.walmart.feeds.api.core.repository.commercialstructure;

import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by vn0y942 on 08/08/17.
 */
public interface CommercialStructureRepository extends JpaRepository<PartnerEntity, UUID> {
}
