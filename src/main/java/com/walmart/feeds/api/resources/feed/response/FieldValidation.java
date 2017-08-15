package com.walmart.feeds.api.resources.feed.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldValidation {

    private String name;
    private String validationMessage;
    private Object rejectedValue;

}

