package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CommercialStructureService {

    void processFile(String partnerSlug, MultipartFile importedFile) throws IOException;

    void removeEntityBySlug(String partnerSlug, String slug);

    List<CommercialStructureEntity> fetchCommercialStructure(String partnerSlug, String slug);

    CommercialStructureEntity fetchOneCommercialStructure(String partnerSlug, String slug);

    CommercialStructureEntity saveWithHistory(CommercialStructureEntity entity);
}
