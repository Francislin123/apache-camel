package com.walmart.feeds.api.resources.template.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemplateResponse {

    private String name;

    private String slug;

    private String header;

    private String body;

    private String footer;

    private String separator;

    private String format;

    private String language;

    private String country;

    private String encoding;

    private String numberFormat;

}