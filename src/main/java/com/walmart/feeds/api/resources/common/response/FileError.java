package com.walmart.feeds.api.resources.common.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class FileError implements ErrorElementResponse {

    private Integer line;
    private String message;

}
