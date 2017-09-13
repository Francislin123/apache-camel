package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.UserException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class TagAdminErrorDecoder implements ErrorDecoder {
    final ErrorDecoder defaultErrorDecoder = new Default();

    public TagAdminErrorDecoder() {
    }

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() >= 400 && response.status() <= 599) {
            return new UserException(String.format("TagAdmin collection not found!"));
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}