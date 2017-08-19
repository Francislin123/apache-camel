package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.ImportStatus;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
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
public class CommercialStructureRouteBuilder extends RouteBuilder {

    public static final String VALIDATE_FILE_ROUTE = "direct:validateFileFormat";
    public static final String PARSE_BINDY_ROUTE = "direct:parseBindy";
    public static final String PERSIST_COMMERCIAL_STRUCTURE_ROUTE = "direct:persistCommercialStructure";

    @Autowired
    private CommercialStructureService commercialStructureService;

    public CommercialStructureRouteBuilder(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() {

        final DataFormat bindy = new BindyCsvDataFormat(CommercialStructureBindy.class);

        from(VALIDATE_FILE_ROUTE)
            .doTry()
                .unmarshal(bindy)
            .doCatch(IllegalArgumentException.class)
                .process(exchange -> {
                    throw new UserException(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class).getMessage());
                });


        from(PARSE_BINDY_ROUTE)
                .process(exchange -> {
                    List<CommercialStructureBindy> body = (List<CommercialStructureBindy>) exchange.getIn().getBody(List.class);

                    final CommercialStructureEntity commercialStructure = exchange.getIn().getHeader("commercialStructure", CommercialStructureEntity.class);

                    List<CommercialStructureAssociationEntity> associationsList = body.stream().map(b ->
                            CommercialStructureAssociationEntity.builder()
                                    .structurePartnerId(b.getStructurePartnerId())
                                    .walmartTaxonomy(b.getWalmartTaxonomy())
                                    .partnerTaxonomy(b.getPartnerTaxonomy())
                                    .commercialStructure(commercialStructure)
                                    .build()
                    ).collect(Collectors.toList());

                    exchange.getOut().setBody(CommercialStructureEntity.builder()
                            .id(commercialStructure.getId())
                            .archiveName(commercialStructure.getArchiveName())
                            .partner(commercialStructure.getPartner())
                            .slug(commercialStructure.getSlug())
                            .associationEntityList(associationsList)
                            .status(ImportStatus.PENDING)
                            .creationDate(commercialStructure.getCreationDate())
                            .updateDate(commercialStructure.getUpdateDate())
                            .user(commercialStructure.getUser())
                            .build());
                });

        from(PERSIST_COMMERCIAL_STRUCTURE_ROUTE)
                .process(exchange -> commercialStructureService.saveWithHistory(exchange.getIn().getBody(CommercialStructureEntity.class)));

    }

}
