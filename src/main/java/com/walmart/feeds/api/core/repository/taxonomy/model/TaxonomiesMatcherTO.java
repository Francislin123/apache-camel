package com.walmart.feeds.api.core.repository.taxonomy.model;


import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class TaxonomiesMatcherTO {

    /**
     * Matched taxonomies. <br>
     * The key should be the walmart taxonomies
     */
    private Map<String, String> matched;

    private List<String> nonMatched;

}
