package com.walmart.feeds.api.core.repository.taxonomy.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaxonomiesMatchedTO {

    private String walmartTaxonomy;

    private String partnerTaxonomy;

}
