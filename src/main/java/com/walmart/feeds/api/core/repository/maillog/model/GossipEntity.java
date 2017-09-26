package com.walmart.feeds.api.core.repository.maillog.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class GossipEntity {
    private String toMail;
    private String templateName;
    private Map<String, String> tags;
    private String error;
}
