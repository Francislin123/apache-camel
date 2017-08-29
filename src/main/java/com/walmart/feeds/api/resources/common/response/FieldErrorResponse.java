package com.walmart.feeds.api.resources.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorResponse implements ErrorElementResponse {

    private String name;
    private String validationMessage;
    private Object rejectedValue;

}

