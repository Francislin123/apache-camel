package com.walmart.feeds.api.core.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserException extends InternalArchitectureException {

    private List<String> exceptionList;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, List<String> exceptionList) {
        super(message);
        this.exceptionList = exceptionList;
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.BAD_REQUEST;
    }

    public List<String> getExceptionList() {
        return exceptionList;
    }

}
