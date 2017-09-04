package com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions;

import com.walmart.feeds.api.core.exceptions.UserException;

public class TaxonomyBlacklistNotFoundException extends UserException {

    public TaxonomyBlacklistNotFoundException(String message) {
        super(message);
    }

    public TaxonomyBlacklistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
