package com.walmart.feeds.api.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.LinkedList;
import java.util.List;

public class LinkedListAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Message newIn = newExchange.getIn();

        TaxonomyMappingBindy newBody = newIn.getBody(TaxonomyMappingBindy.class);

        List list = null;

        if (oldExchange == null) {
            list = new LinkedList<>();
            list.add(newBody);
            newIn.setBody(list);
            return newExchange;

        } else {
            Message in = oldExchange.getIn();
            list = in.getBody(List.class);
            list.add(newBody);
            return oldExchange;
        }
    }

}
