package com.walmart.feeds.api.core.service.fields;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;

public interface FieldsMappingService {

    FieldsMappingEntity findBySlug(String slug) throws EntityNotFoundException;

    void updateFieldsMapping(FieldsMappingEntity mappingEntity) throws EntityNotFoundException;

}
