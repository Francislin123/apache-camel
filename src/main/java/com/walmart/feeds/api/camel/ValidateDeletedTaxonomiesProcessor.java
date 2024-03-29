package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.resources.common.response.FileError;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.ERROR_LIST;
import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;

@Component
public class ValidateDeletedTaxonomiesProcessor implements Processor {

    @Autowired
    private TaxonomyBlacklistService taxonomyBlacklistService;

    @Override
    public void process(Exchange exchange) throws Exception {

        List<TaxonomyMappingEntity> body = (List<TaxonomyMappingEntity>) exchange.getIn().getBody(List.class);

        final PartnerTaxonomyEntity partnerTaxonomy = exchange.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class);

        if (partnerTaxonomy == null) {
            throw new SystemException("PartnerTaxonomy must exists in route");
        }

        if (partnerTaxonomy.getTaxonomyMappings() != null) {

            List<TaxonomyMappingEntity> removedTaxonomies = new LinkedList<>(partnerTaxonomy.getTaxonomyMappings());
            removedTaxonomies.removeAll(body);

            List<FileError> errorsList = exchange.getIn().getHeader(ERROR_LIST, List.class);
            Set<String> taxonomies = new HashSet<>();

            removedTaxonomies.forEach(t -> {
                taxonomies.add(t.getPartnerPath());
                taxonomies.add(t.getWalmartPath());
            });

            taxonomies.forEach(t -> {
                List<TaxonomyBlacklistEntity> taxonomyBlacklistEntity = taxonomyBlacklistService.findBlackList(t);
                if (taxonomyBlacklistEntity != null && !taxonomyBlacklistEntity.isEmpty()) {
                    List<String> taxonomiesSlug = taxonomyBlacklistEntity.stream().map(TaxonomyBlacklistEntity::getSlug).collect(Collectors.toList());
                    errorsList.add(FileError.builder()
                            .message(String.format("Cannot remove taxonomy=\"%s\" because it is associated to the following blacklists slug=%s", t, taxonomiesSlug))
                            .build());
                }
            });

        }

    }
}
