package com.walmart.feeds.api.core.service.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.resources.taxonomy.request.UploadTaxonomyMappingTO;

import java.io.IOException;
import java.util.List;


public interface PartnerTaxonomyService {

    void processFile(UploadTaxonomyMappingTO uploadTaxonomyMappingTO) throws IOException;

    void removeEntityBySlug(String partnerSlug, String slug);

    List<PartnerTaxonomyEntity> fetchPartnerTaxonomies(String partnerSlug, String slug);

    PartnerTaxonomyEntity fetchProcessedPartnerTaxonomy(String partnerSlug, String slug);

    PartnerTaxonomyEntity saveWithHistory(PartnerTaxonomyEntity entity);
}
