package com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions;

import com.walmart.feeds.api.core.exceptions.UserException;

public class TermsBlacklistNotFoundException extends UserException {

    public TermsBlacklistNotFoundException(String message) {
        super(message);
    }

    public TermsBlacklistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}


