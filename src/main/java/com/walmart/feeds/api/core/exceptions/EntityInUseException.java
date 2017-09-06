package com.walmart.feeds.api.core.exceptions;

import com.walmart.feeds.api.resources.common.response.SimpleError;

import java.util.List;

public class EntityInUseException extends UserException {

    public EntityInUseException(String message) {
        super(message);
    }

    public EntityInUseException(String message, List<SimpleError> exceptionList) {
        super(message, exceptionList);
    }

    public EntityInUseException(String message, Throwable cause) {
        super(message, cause);
    }

}
