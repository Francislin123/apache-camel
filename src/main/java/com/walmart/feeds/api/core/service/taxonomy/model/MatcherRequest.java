package com.walmart.feeds.api.core.service.taxonomy.model;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatcherRequest {

    private List<String> taxonomies;

}
