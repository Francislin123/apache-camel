package com.walmart.feeds.api.resources.taxonomy.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TaxonomyReportResponse {

    private Integer totalItemsImported;
    private List<TaxonomyMappingResponse> removed;
    private List<TaxonomyMappingResponse> added;

}
