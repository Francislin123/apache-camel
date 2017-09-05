package com.walmart.feeds.api.core.exceptions;

import com.walmart.feeds.api.resources.common.response.FileError;

import java.util.List;

public class InvalidFileException extends UserException {

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileException(String message, List<FileError> errors) {
        super(message);
        this.errors = errors;
    }

}
