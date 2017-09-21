package com.walmart.feeds.api.resources.blacklist.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TermsBlacklistResponse {

    private String name;

    private String slug;

    private Set<String> list;

}
