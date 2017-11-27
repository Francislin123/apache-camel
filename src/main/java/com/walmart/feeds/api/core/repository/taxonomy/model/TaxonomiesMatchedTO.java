package com.walmart.feeds.api.core.repository.taxonomy.model;


import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class TaxonomiesMatchedTO implements Serializable {

    private String walmartTaxonomy;

    private String partnerTaxonomy;

}
