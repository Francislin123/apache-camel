package com.walmart.feeds.api.client.tagadmin;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Created by vn0gshm on 25/08/17.
 */
public interface TagAdmimCollectionClient {

    @RequestLine("GET /tags/{id}")
    @Headers("Content-Type: application/json")
    TagAdminCollection findById(@Param("id") Long id);
}