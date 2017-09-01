package com.walmart.feeds.api.camel;

import com.walmart.feeds.api.core.exceptions.InvalidFileException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.ERROR_LIST;

@Component
public class ValidateRouteWithErrorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        List errorList = exchange.getIn().getHeader(ERROR_LIST, List.class);

        if (!errorList.isEmpty()) {
            throw new InvalidFileException("Invalid file content", errorList);
        }
        exchange.getIn().getHeader(ERROR_LIST, List.class).clear();
//        exchange.getProperties().clear();

    }
}
