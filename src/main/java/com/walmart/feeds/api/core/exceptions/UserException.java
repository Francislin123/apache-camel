package com.walmart.feeds.api.core.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends InternalArchitectureException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
