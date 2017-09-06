package com.walmart.feeds.api.resources.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldError implements ErrorElementResponse {

    private String name;
    private String message;
    private Object rejectedValue;

}

