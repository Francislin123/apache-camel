package com.walmart.feeds.api.core.repository.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface CommercialStructureAssociationRepository extends JpaRepository<CommercialStructureAssociationEntity, UUID> {

}
