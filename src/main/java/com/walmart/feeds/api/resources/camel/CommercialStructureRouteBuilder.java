package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.resources.commercialstructure.service.CommercialStructureService;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class CommercialStructureRouteBuilder extends RouteBuilder{

    @Autowired
    private CommercialStructureProcessor commercialStructureProcessor;

    @Autowired
    private CommercialStructureService commercialStructureService;

    @Override
    public void configure() throws EntityNotFoundException {
        //RECEBER UM CVS -> TRANSFORMAR EM UMA LISTA DE ENTITY -> PRINTAR RESULT
        final DataFormat bindy = new BindyCsvDataFormat(CommercialStructureBindy.class);
        from("direct:test").unmarshal(bindy).doTry().bean(commercialStructureProcessor, "process").doCatch(IllegalArgumentException.class)
                .log("pegou").bean(commercialStructureService).end();

    }

}
