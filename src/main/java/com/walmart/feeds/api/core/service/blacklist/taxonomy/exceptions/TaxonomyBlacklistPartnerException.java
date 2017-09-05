package com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions;

import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.resources.common.response.SimpleError;

import java.util.List;
import java.util.stream.Collectors;

public class TaxonomyBlacklistPartnerException extends UserException {

    public TaxonomyBlacklistPartnerException(String message) {
        super(message);
    }

    public TaxonomyBlacklistPartnerException(String message, List<String> exceptionList) {
        super(message, exceptionList.stream().map(f -> SimpleError.builder().message(f).build()).collect(Collectors.toList()));
    }

    public TaxonomyBlacklistPartnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
