package com.walmart.feeds.api.client.tagadmin;

import com.walmart.feeds.api.client.categoryCollection.CategoryClient;
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

    @RequestLine("GET categories?name={name}&page=1&page_size=10&displays=category_parent_deep")
    @Headers("Content-Type: application/json")
    CategoryClient findByCategoryName(@Param("name") String name);

}
