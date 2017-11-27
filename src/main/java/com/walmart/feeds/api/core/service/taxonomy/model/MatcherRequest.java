package com.walmart.feeds.api.core.service.taxonomy.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MatcherRequest implements Serializable {

    private String walmartTaxonomy;

}
