package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;

/**
 * Created by vn0y942 on 15/08/17.
 */
public interface CommercialStructureService {

    void loadFile(CommercialStructureEntity commercialStructureEntity);

    CommercialStructureHistory entityToHistoryTransform(CommercialStructureEntity commercialStructureEntity);
}
