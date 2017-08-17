package com.walmart.feeds.api.resources.commercialstructure.response;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommercialStructureResponse {

    private String slug;

    private String archiveName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime mappingDate;

    private String link;
}
