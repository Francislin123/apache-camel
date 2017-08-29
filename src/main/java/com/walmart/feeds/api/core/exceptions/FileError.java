package com.walmart.feeds.api.core.exceptions;

import com.walmart.feeds.api.resources.common.response.ErrorElementResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileError implements ErrorElementResponse {

    private Integer line;
    private String message;

}
