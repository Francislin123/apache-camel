package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;

@Component
public class FillPartnerTaxonomiesProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        final PartnerTaxonomyEntity partnerTaxonomy = exchange.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class);
        List<TaxonomyMappingEntity> associationsList = exchange.getIn().getBody(List.class);

        List<TaxonomyMappingEntity> taxonomiesToPersist = null;

        if (partnerTaxonomy.getTaxonomyMappings() != null) {

            taxonomiesToPersist = new ArrayList<>(partnerTaxonomy.getTaxonomyMappings());

            //Remove taxonomies that are not longer in file
            taxonomiesToPersist.retainAll(associationsList);

            //Segregate taxonomies to be added
            associationsList.removeAll(partnerTaxonomy.getTaxonomyMappings());

            //Add segregated taxonomies to the persisted list
            taxonomiesToPersist.addAll(associationsList);

        } else {
            taxonomiesToPersist = associationsList;
        }

        exchange.getOut().setBody(PartnerTaxonomyEntity.builder()
                .id(partnerTaxonomy.getId())
                .fileName(partnerTaxonomy.getFileName())
                .name(partnerTaxonomy.getName())
                .partner(partnerTaxonomy.getPartner())
                .slug(partnerTaxonomy.getSlug())
                .taxonomyMappings(taxonomiesToPersist)
                .status(ImportStatus.PENDING)
                .creationDate(partnerTaxonomy.getCreationDate())
                .updateDate(partnerTaxonomy.getUpdateDate())
                .user(partnerTaxonomy.getUser())
                .build());

    }
}
