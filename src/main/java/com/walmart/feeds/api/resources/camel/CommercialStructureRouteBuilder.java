package com.walmart.feeds.api.resources.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class CommercialStructureRouteBuilder extends RouteBuilder{

    @Autowired
    private CvsToCommercialStructureProcessor cvsToCommercialStructureProcessor;

    @Override
    public void configure() throws Exception {
        //RECEBER UM CVS -> TRANSFORMAR EM UMA LISTA DE ENTITY -> PRINTAR RESULT
        final DataFormat bindy = new BindyCsvDataFormat(CommercialStructureBindy.class);
        from("direct:test").unmarshal(bindy).log("${body}").bean(cvsToCommercialStructureProcessor, "process")
                .log("${body}");

    }

}
