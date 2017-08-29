package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.FileError;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.ERROR_LIST;
import static org.apache.camel.Exchange.SPLIT_INDEX;

public class ValidateTaxonomyBindyProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        TaxonomyMappingBindy mappingBindy = exchange.getIn().getBody(TaxonomyMappingBindy.class);

        List<FileError> errors = exchange.getIn().getHeader(ERROR_LIST, List.class);

        if (mappingBindy.getWalmartTaxonomy() == null || mappingBindy.getPartnerTaxonomy() == null || mappingBindy.getStructurePartnerId() == null) {
            errors.add(FileError.builder()
                    .line(exchange.getProperty(SPLIT_INDEX, Integer.class)+1)
                    .message("All columns must be informed")
                .build());
        }

    }
}
