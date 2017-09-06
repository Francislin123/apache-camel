package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;

@Component
public class ParseTaxonomyCsvProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        List<TaxonomyMappingBindy> taxonomyMappingBindyList = (List<TaxonomyMappingBindy>) exchange.getIn().getBody(List.class);

        final PartnerTaxonomyEntity partnerTaxonomy = exchange.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class);

        if (partnerTaxonomy == null) {
            throw new SystemException("PartnerTaxonomy must exists in route");
        }

        List<TaxonomyMappingEntity> associationsList = taxonomyMappingBindyList.stream().map(b ->
                TaxonomyMappingEntity.builder()
                        .partnerPathId(b.getStructurePartnerId())
                        .walmartPath(b.getWalmartTaxonomy())
                        .partnerPath(b.getPartnerTaxonomy())
                        .partnerTaxonomy(partnerTaxonomy)
                        .build()
        ).collect(Collectors.toList());

        exchange.getOut().setBody(associationsList);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());

    }
}
