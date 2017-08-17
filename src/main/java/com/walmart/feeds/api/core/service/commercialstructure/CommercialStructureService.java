package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;

import java.util.List;


public interface CommercialStructureService {

    void loadFile(CommercialStructureEntity commercialStructureEntity);

    CommercialStructureHistory entityToHistoryTransform(CommercialStructureEntity commercialStructureEntity);

    void removeEntityBySlug(String partnerSlug, String slug);

    List<CommercialStructureEntity> fetchCommercialStructure(String partnerSlug, String slug);

    CommercialStructureEntity fetchOneCommercialStructure(String partnerSlug, String slug);

}
