package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class PartnerTaxonomyRouteBuilder extends RouteBuilder {

    public static final String VALIDATE_FILE_ROUTE = "direct:validateFileFormat";
    public static final String PARSE_BINDY_ROUTE = "direct:parseBindy";
    public static final String PERSIST_PARTNER_TAXONOMY_ROUTE = "direct:persistCommercialStructure";
    public static final String PERSISTED_PARTNER_TAXONOMY = "partnerTaxonomy";

    @Autowired
    private PartnerTaxonomyService partnerTaxonomyService;

    public PartnerTaxonomyRouteBuilder(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() {

        final DataFormat bindy = new BindyCsvDataFormat(TaxonomyMappingBindy.class);

        from(VALIDATE_FILE_ROUTE)
            .doTry()
                .unmarshal(bindy)
            .doCatch(IllegalArgumentException.class)
                .process(exchange -> {
                    throw new UserException(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class).getMessage());
                });


        from(PARSE_BINDY_ROUTE)
                .process(exchange -> {
                    List<TaxonomyMappingBindy> body = (List<TaxonomyMappingBindy>) exchange.getIn().getBody(List.class);

                    final PartnerTaxonomyEntity partnerTaxonomy = exchange.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class);

                    List<TaxonomyMappingEntity> associationsList = body.stream().map(b ->
                            TaxonomyMappingEntity.builder()
                                    .partnerPathId(b.getStructurePartnerId())
                                    .walmartPath(b.getWalmartTaxonomy())
                                    .partnerPath(b.getPartnerTaxonomy())
                                    .partnerTaxonomy(partnerTaxonomy)
                                    .build()
                    ).collect(Collectors.toList());

                    exchange.getOut().setBody(PartnerTaxonomyEntity.builder()
                            .id(partnerTaxonomy.getId())
                            .fileName(partnerTaxonomy.getFileName())
                            .name(partnerTaxonomy.getName())
                            .partner(partnerTaxonomy.getPartner())
                            .slug(partnerTaxonomy.getSlug())
                            .taxonomyMappings(associationsList)
                            .status(ImportStatus.PENDING)
                            .creationDate(partnerTaxonomy.getCreationDate())
                            .updateDate(partnerTaxonomy.getUpdateDate())
                            .user(partnerTaxonomy.getUser())
                            .build());
                });

        from(PERSIST_PARTNER_TAXONOMY_ROUTE)
                .process(exchange -> partnerTaxonomyService.saveWithHistory(exchange.getIn().getBody(PartnerTaxonomyEntity.class)));

    }

}
