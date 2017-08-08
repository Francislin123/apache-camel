package com.walmart.feeds.api.core.service.fields;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;

public interface FieldsMappingService {

    FieldsMappingEntity findBySlug(String slug) throws NotFoundException;

    void updateFieldsMapping(FieldsMappingEntity mappingEntity) throws NotFoundException;

}
