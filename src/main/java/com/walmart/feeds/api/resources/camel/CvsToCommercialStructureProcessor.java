package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class CvsToCommercialStructureProcessor {

    @Autowired
    PartnerService partnerService;

    public List<CommercialStructureEntity> process(Exchange exchange) throws NotFoundException {
        List<CommercialStructureEntity> returnList = new ArrayList<>();

        PartnerEntity partner = new PartnerEntity();
        //TODO next step
//        PartnerEntity partner = partnerService.findBySlug(exchange.getIn().getHeader("partnerSlug").toString());
        //TODO Create line validation
        for (CommercialStructureBindy bindy: (List<CommercialStructureBindy>)exchange.getIn().getBody()) {
            returnList.add(CommercialStructureEntity.builder().structurePartnerId(bindy.getStructurePartnerId())
                    .partnerTaxonomy(bindy.getPartnerTaxonomy()).walmartTaxonomy(bindy.getWalmartTaxonomy())
                    .archiveName(exchange.getIn().getHeader("archiveName").toString())
                    .partner(partner).slug(SlugParserUtil.toSlug(exchange.getIn().getHeader("archiveName").toString())).build());
        }
        return returnList;
    }
}
