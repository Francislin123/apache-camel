package com.walmart.feeds.api.client.tags;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * Created by vn0gshm on 25/08/17.
 */
public interface TagAdmimCollectionClient {

    @RequestLine("GET /tags/{id}")
    @Headers("Content-Type: application/json")
    TagAdminCollection findById(@Param("id") long id);
}
