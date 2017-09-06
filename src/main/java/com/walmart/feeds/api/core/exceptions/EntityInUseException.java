package com.walmart.feeds.api.core.exceptions;

import java.util.List;

public class EntityInUseException extends UserException {

    public EntityInUseException(String message) {
        super(message);
    }

    public EntityInUseException(String message, List<String> exceptionList) {
        super(message, exceptionList);
    }

    public EntityInUseException(String message, Throwable cause) {
        super(message, cause);
    }

}
