package com.walmart.feeds.api.resources.blacklist.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistMappingRequest;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class TaxonomyBlacklistResponse {

    private String name;

    private String slug;

    private Set<TaxonomyBlacklistMappingRequest> list;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

}
