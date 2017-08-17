package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureAssociationRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class CommercialStructureProcessor implements Processor {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private CommercialStructureService commercialStructureService;

    @Autowired
    private CommercialStructureAssociationRepository commercialStructureAssociationRepository;

    @Override
    @Transactional
    public void process(Exchange exchange){

        CommercialStructureBindy c = exchange.getIn().getBody(CommercialStructureBindy.class);
        System.out.println("Id: " + Thread.currentThread().getId() +  " Name: \"" + Thread.currentThread().getName() + "\" Object: \"" + c.getStructurePartnerId() + "\"");
        commercialStructureAssociationRepository.saveAndFlush(CommercialStructureAssociationEntity.builder()
                .structurePartnerId(c.getStructurePartnerId())
                .walmartTaxonomy(c.getWalmartTaxonomy())
                .partnerTaxonomy(c.getPartnerTaxonomy())
                .commercialStructure(exchange.getIn().getHeader("commercialStructure", CommercialStructureEntity.class))
                .build());

        exchange.getOut().setBody(exchange.getIn().getBody());
    }
}
