package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import org.springframework.data.domain.Page;

/**
 * Created by vn0y942 on 15/08/17.
 */
public interface CommercialStructureService {

    void loadFile(CommercialStructureEntity commercialStructureEntity);

    CommercialStructureHistory entityToHistoryTransform(CommercialStructureEntity commercialStructureEntity);

    void removeEntityBySlug(String partnerSlug, String slug);

    Page<CommercialStructureEntity> fetchBySlug(String partnerSlug, String slug, int page, int size);
}
