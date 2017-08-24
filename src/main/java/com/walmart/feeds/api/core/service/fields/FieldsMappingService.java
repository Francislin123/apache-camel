package com.walmart.feeds.api.core.service.fields;

import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;

import java.util.List;

public interface FieldsMappingService {

    /**
     * @param fieldsMappingEntity payload
     */
    void save(FieldsMappingEntity fieldsMappingEntity);

    void update(FieldsMappingEntity mappingEntity);

    void delete(String fieldsMappingEntity);

    FieldsMappingEntity findBySlug(String slug);

    List<FieldsMappingEntity> findAll();

    void hasConflict(String slug);
}
