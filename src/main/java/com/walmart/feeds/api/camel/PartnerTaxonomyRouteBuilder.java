package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.InvalidFileException;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
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
    public static final String ERROR_LIST = "errorList";

    @Autowired
    private PartnerTaxonomyService partnerTaxonomyService;

    public PartnerTaxonomyRouteBuilder(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() {

        from(VALIDATE_FILE_ROUTE)
                .setHeader(ERROR_LIST, LinkedList::new)
                .unmarshal()
                    .bindy(BindyType.Csv, TaxonomyMappingBindy.class)
                .split(body(), new LinkedListAggregationStrategy())
                    .process(new ValidateTaxonomyBindyProcessor())
                .end()
                .process(exchange -> {
                    List errorList = exchange.getIn().getHeader(ERROR_LIST, List.class);

                    if (!errorList.isEmpty()) {
                        throw new InvalidFileException("Invalid file content", errorList);
                    }
                    exchange.getIn().getHeaders().clear();
                    exchange.getProperties().clear();

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
                });

        from(PERSIST_PARTNER_TAXONOMY_ROUTE)
                .process(exchange -> partnerTaxonomyService.saveWithHistory(exchange.getIn().getBody(PartnerTaxonomyEntity.class)));

    }

}