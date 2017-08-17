package com.walmart.feeds.api.core.service.fields;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;

import java.util.List;

public interface FieldsMappingService {

    /**
     * @param fieldsMappingEntity payload
     */
    void save(FieldsMappingEntity fieldsMappingEntity) throws IllegalArgumentException;

    void update(FieldsMappingEntity mappingEntity) throws EntityNotFoundException;

    void delete(String fieldsMappingEntity);

    FieldsMappingEntity findBySlug(String slug) throws EntityNotFoundException;

    List<FieldsMappingEntity> findAll();

    void hasConflict(String slug) throws EntityAlreadyExistsException;
}
