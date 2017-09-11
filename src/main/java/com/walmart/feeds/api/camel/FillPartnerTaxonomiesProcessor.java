package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.service.taxonomy.model.TaxonomyUploadReportTO;
import com.walmart.feeds.api.core.utils.MergeListUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;

@Component
public class FillPartnerTaxonomiesProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        final PartnerTaxonomyEntity partnerTaxonomy = exchange.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class);

        if (partnerTaxonomy == null) {
            throw new SystemException("PartnerTaxonomy must exists in route");
        }

        TaxonomyUploadReportTO reportTO = new TaxonomyUploadReportTO();

        List<TaxonomyMappingEntity> taxonomiesFromFile = exchange.getIn().getBody(List.class);
        List<TaxonomyMappingEntity> persistedTaxonomies = partnerTaxonomy.getTaxonomyMappings();

        List<TaxonomyMappingEntity> insertedItems = MergeListUtils.getDiffItems(taxonomiesFromFile, persistedTaxonomies);
        List<TaxonomyMappingEntity> removedItems = MergeListUtils.getDiffItems(persistedTaxonomies, taxonomiesFromFile);

        List<TaxonomyMappingEntity> taxonomiesToPersist = persistedTaxonomies;

        if (persistedTaxonomies == null) {
            taxonomiesToPersist = new LinkedList<>();
        }

        if (removedItems != null) {
            taxonomiesToPersist.removeAll(removedItems);
        }

        taxonomiesToPersist.addAll(insertedItems);

        reportTO.setItemsImported(taxonomiesToPersist == null ? null : taxonomiesToPersist.size());
        reportTO.setTaxonomiesToInsert(insertedItems);
        reportTO.setTaxonomiesToRemove(removedItems);

        reportTO.setEntityToSave(PartnerTaxonomyEntity.builder()
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

        reportTO.setStatus(reportTO.getEntityToSave().getStatus());

        exchange.getOut().setBody(reportTO);

    }
}
