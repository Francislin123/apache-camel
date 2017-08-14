package com.walmart.feeds.api.resources.commercialstructure.service;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;

/**
 * Created by vn0y942 on 08/08/17.
 */
public interface CommercialStructureService {

    void loadCommercialStructure(CommercialStructureEntity commercialStructureEntity) throws EntityNotFoundException;

}
