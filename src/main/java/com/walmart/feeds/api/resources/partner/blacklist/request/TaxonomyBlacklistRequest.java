package com.walmart.feeds.api.resources.partner.blacklist.request;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class TaxonomyBlacklistRequest {


    private String name;

    private List<TaxonomyBlacklistMapping> list;

}
