package com.walmart.feeds.api.resources.common.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileError implements ErrorElementResponse {

    private Integer line;
    private String message;

}
