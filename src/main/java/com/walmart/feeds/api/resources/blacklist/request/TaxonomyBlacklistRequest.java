package com.walmart.feeds.api.resources.blacklist.request;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Data
@Validated
public class TaxonomyBlacklistRequest {


    private String name;

    private Set<TaxonomyBlacklistMapping> list;

}
