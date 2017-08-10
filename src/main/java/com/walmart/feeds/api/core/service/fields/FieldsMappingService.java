package com.walmart.feeds.api.core.service.fields;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;

import java.util.List;

public interface FieldsMappingService {

    /**
     * @param fieldsMappingEntity payload
     */
    void saveFieldsdMapping(FieldsMappingEntity fieldsMappingEntity) throws IllegalArgumentException;

    void updateFieldsMapping(FieldsMappingEntity mappingEntity) throws EntityNotFoundException;

    void deleteFieldsMapping(FieldsMappingEntity fieldsMappingEntity);

    FieldsMappingEntity findBySlug(String slug) throws EntityNotFoundException;

    List<FieldsMappingEntity> findAllFieldsMapping();

}
