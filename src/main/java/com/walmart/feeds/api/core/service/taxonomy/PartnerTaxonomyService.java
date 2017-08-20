package com.walmart.feeds.api.core.service.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface PartnerTaxonomyService {

    void processFile(String partnerSlug, MultipartFile importedFile) throws IOException;

    void removeEntityBySlug(String partnerSlug, String slug);

    List<PartnerTaxonomyEntity> fetchPartnerTaxonomies(String partnerSlug, String slug);

    PartnerTaxonomyEntity fetchPartnerTaxonomy(String partnerSlug, String slug);

    PartnerTaxonomyEntity saveWithHistory(PartnerTaxonomyEntity entity);
}
