package com.walmart.feeds.api.core.repository.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommercialStructureAssociationRepository extends JpaRepository<CommercialStructureAssociationEntity, UUID> {

}
