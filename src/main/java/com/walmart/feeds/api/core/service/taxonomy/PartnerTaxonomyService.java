package com.walmart.feeds.api.core.service.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.model.TaxonomyUploadReportTO;
import com.walmart.feeds.api.core.service.taxonomy.model.UploadTaxonomyMappingTO;

import java.io.IOException;
import java.util.List;


public interface PartnerTaxonomyService {

    TaxonomyUploadReportTO processFile(UploadTaxonomyMappingTO uploadTaxonomyMappingTO) throws IOException;

    void removeEntityBySlug(String partnerSlug, String slug);

    List<PartnerTaxonomyEntity> fetchPartnerTaxonomies(String partnerSlug, String slug);

    PartnerTaxonomyEntity fetchProcessedPartnerTaxonomy(String partnerSlug, String slug);

    PartnerTaxonomyEntity saveWithHistory(PartnerTaxonomyEntity entity);
}
