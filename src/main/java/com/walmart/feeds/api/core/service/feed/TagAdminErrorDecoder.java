package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.UserException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class TagAdminErrorDecoder implements ErrorDecoder {

    public TagAdminErrorDecoder() {
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        return new UserException("TagAdmin collection not found!");
    }
}