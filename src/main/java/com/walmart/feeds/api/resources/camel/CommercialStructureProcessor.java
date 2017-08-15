package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class CommercialStructureProcessor {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private CommercialStructureService commercialStructureService;

    @Transactional
    public void process(Exchange exchange){

        PartnerEntity partner = partnerService.findBySlug(exchange.getIn().getHeader("partnerSlug").toString());
        CommercialStructureEntity entity = CommercialStructureEntity.builder()
                .slug(SlugParserUtil.toSlug(exchange.getIn().getHeader("archiveName").toString()))
                .partner(partner)
                .archiveName(exchange.getIn().getHeader("archiveName").toString())
                .associationEntityList(((List<CommercialStructureBindy>)exchange.getIn().getBody()).stream().map(bindy -> (
                    CommercialStructureAssociationEntity.builder()
                            .structurePartnerId(bindy.getStructurePartnerId())
                            .partnerTaxonomy(bindy.getPartnerTaxonomy())
                            .walmartTaxonomy(bindy.getWalmartTaxonomy())
                            .build())).collect(Collectors.toList()))
                .build();
        //TODO Create line validation
        commercialStructureService.loadFile(entity);
    }
}
