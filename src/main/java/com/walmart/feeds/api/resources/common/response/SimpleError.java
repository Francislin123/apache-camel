package com.walmart.feeds.api.resources.common.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleError implements ErrorElementResponse {

    private String message;

}
