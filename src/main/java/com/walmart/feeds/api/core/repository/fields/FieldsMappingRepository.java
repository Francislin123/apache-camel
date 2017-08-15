package com.walmart.feeds.api.core.repository.fields;

import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by vn0gshm on 07/08/17.
 */
@Repository
public interface FieldsMappingRepository extends JpaRepository<FieldsMappingEntity, UUID> {

    Optional<FieldsMappingEntity> findBySlug(String slug);

}
