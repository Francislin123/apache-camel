package com.walmart.feeds.api.core.repository.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommercialStructureRepository extends JpaRepository<CommercialStructureEntity, UUID> {

    Optional<CommercialStructureEntity> findBySlug(String slug);

}
