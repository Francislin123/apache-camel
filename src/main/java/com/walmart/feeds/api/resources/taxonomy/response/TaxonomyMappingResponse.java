package com.walmart.feeds.api.resources.taxonomy.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TaxonomyMappingResponse {

    private String partnerPathId;
    private String partnerPath;
    private String walmartPath;

}
