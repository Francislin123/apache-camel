package com.walmart.feeds.api.core.persistence.elasticsearch;


import java.util.List;

public interface ElasticSearchService {

    boolean validateWalmartTaxonomy(String taxonomy);

    List<String> getSkuFieldsMapping();

}
