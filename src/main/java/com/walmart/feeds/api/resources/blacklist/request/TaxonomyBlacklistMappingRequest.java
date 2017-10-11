package com.walmart.feeds.api.resources.blacklist.request;

import com.walmart.feeds.api.resources.feed.validator.annotation.ValidTaxonomyOwner;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@Builder
@Getter
@EqualsAndHashCode
public class TaxonomyBlacklistMappingRequest {

    @NotBlank
    private String taxonomy;

    @NotEmpty
    @ValidTaxonomyOwner
    private String owner;

    @Tolerate
    public TaxonomyBlacklistMappingRequest() {
        // default
    }
}
