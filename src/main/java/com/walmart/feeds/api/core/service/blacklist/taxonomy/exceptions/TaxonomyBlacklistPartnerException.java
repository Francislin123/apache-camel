package com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions;

import com.walmart.feeds.api.core.exceptions.UserException;

public class TaxonomyBlacklistPartnerException extends UserException {

    public TaxonomyBlacklistPartnerException(String message) {
        super(message);
    }

    public TaxonomyBlacklistPartnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
