package com.walmart.feeds.api.core.exceptions;

import java.util.List;

public class InvalidFileException extends UserException {

    private List<FileError> errors;

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

    public List<FileError> getErrors() {
        return errors;
    }
}
