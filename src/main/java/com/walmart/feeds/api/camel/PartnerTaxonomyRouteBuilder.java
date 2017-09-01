package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class PartnerTaxonomyRouteBuilder extends RouteBuilder {

    public static final String VALIDATE_FILE_ROUTE = "direct:validateFileFormat";
    public static final String PARSE_FILE_ROUTE = "direct:parseBindy";
    public static final String PERSIST_PARTNER_TAXONOMY_ROUTE = "direct:persistCommercialStructure";
    public static final String PERSISTED_PARTNER_TAXONOMY = "partnerTaxonomy";
    public static final String ERROR_LIST = "errorList";

    @Autowired
    private PartnerTaxonomyService partnerTaxonomyService;

    @Autowired
    private ValidateTaxonomyBindyProcessor validateTaxonomyBindyProcessor;

    @Autowired
    private ValidateRouteWithErrorProcessor validateRouteWithErrorProcessor;

    @Autowired
    private ParseTaxonomyCsvProcessor parseTaxonomyCsvProcessor;

    @Autowired
    private RollBackTaxonomyProcessor rollbackTaxonomyProcessor;

    @Autowired
    private ValidateDeletedTaxonomiesProcessor validateDeletedTaxonomiesProcessor;

    @Autowired
    private FillPartnerTaxonomiesProcessor fillPartnerTaxonomiesProcessor;

    public PartnerTaxonomyRouteBuilder(CamelContext context) {
        super(context);
    }

    @Override
    public void configure() {

        from(VALIDATE_FILE_ROUTE)
                .setHeader(ERROR_LIST, LinkedList::new)
                .doTry()
                .unmarshal()
                    .bindy(BindyType.Csv, TaxonomyMappingBindy.class)
                .endDoTry()
                .doCatch(IllegalArgumentException.class)
                    .throwException(new UserException("Invalid file content"))
                .end()
                .split(body(), new LinkedListAggregationStrategy())
                    .process(validateTaxonomyBindyProcessor)
                .end()
                .process(validateRouteWithErrorProcessor);

        from(PARSE_FILE_ROUTE)
                .onException(Exception.class)
                    .process(rollbackTaxonomyProcessor)
                .end()
                .setHeader(ERROR_LIST, LinkedList::new)
                .process(parseTaxonomyCsvProcessor)
                .process(validateDeletedTaxonomiesProcessor)
                .process(validateRouteWithErrorProcessor)
                .process(fillPartnerTaxonomiesProcessor);

        from(PERSIST_PARTNER_TAXONOMY_ROUTE)
                .process(exchange -> partnerTaxonomyService.saveWithHistory(exchange.getIn().getBody(PartnerTaxonomyEntity.class)));

    }

}