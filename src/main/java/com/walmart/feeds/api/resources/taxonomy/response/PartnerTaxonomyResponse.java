package com.walmart.feeds.api.resources.taxonomy.response;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PartnerTaxonomyResponse {

    private String slug;

    private String archiveName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime mappingDate;

    private String link;
}
