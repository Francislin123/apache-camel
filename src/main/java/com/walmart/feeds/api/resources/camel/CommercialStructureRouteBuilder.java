package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class CommercialStructureRouteBuilder extends RouteBuilder {

    private CommercialStructureProcessor commercialStructureProcessor;

    public CommercialStructureRouteBuilder(CamelContext context, CommercialStructureProcessor commercialStructureProcessor) {
        super(context);
        this.commercialStructureProcessor = commercialStructureProcessor;
    }

    @Override
    public void configure() throws EntityNotFoundException {
        //RECEBER UM CVS -> TRANSFORMAR EM UMA LISTA DE ENTITY -> PRINTAR RESULT
        final DataFormat bindy = new BindyCsvDataFormat(CommercialStructureBindy.class);
        from("direct:test")
                .doTry()
                    .unmarshal(bindy)
                    .bean(commercialStructureProcessor, "process")
                .doCatch(IllegalArgumentException.class)
                    .log(exceptionMessage().toString())
                .end();

    }

}
