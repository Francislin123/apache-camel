package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;

@Component
public class RollBackTaxonomyProcessor implements Processor {

    @Autowired
    private PartnerTaxonomyService partnerTaxonomyService;

    @Override
    public void process(Exchange exchange) throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = exchange.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class);

        partnerTaxonomyService.saveWithHistory(PartnerTaxonomyEntity.builder()
                .id(partnerTaxonomy.getId())
                .status(ImportStatus.ERROR)
                .name(partnerTaxonomy.getName())
                .fileName(partnerTaxonomy.getFileName())
                .partner(partnerTaxonomy.getPartner())
                .slug(partnerTaxonomy.getSlug())
                .creationDate(partnerTaxonomy.getCreationDate())
                .taxonomyMappings(partnerTaxonomy.getTaxonomyMappings())
                .build());

    }
}
