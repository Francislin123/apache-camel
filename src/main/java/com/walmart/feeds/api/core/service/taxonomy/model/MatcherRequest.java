package com.walmart.feeds.api.core.service.taxonomy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatcherRequest {

    private String walmartTaxonomy;

}
