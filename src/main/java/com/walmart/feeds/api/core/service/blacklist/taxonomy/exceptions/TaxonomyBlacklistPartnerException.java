package com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions;

import com.walmart.feeds.api.core.exceptions.UserException;

import java.util.List;

public class TaxonomyBlacklistPartnerException extends UserException {

    public TaxonomyBlacklistPartnerException(String message) {
        super(message);
    }

    public TaxonomyBlacklistPartnerException(String message, List<String> exceptionList) {
        super(message, exceptionList);
    }

    public TaxonomyBlacklistPartnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
