package com.walmart.feeds.api.client.tags;

import com.walmart.feeds.api.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by vn0gshm on 25/08/17.
 */
@FeignClient(name = "http://vip-cat-serv.qa.vmcommerce.intra/ws/", decode404 = true, configuration = FeignConfiguration.class)
public interface TagAdmimCollectionClient {

    @RequestMapping(value = "/tags/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<TagAdminCollection> findById(@PathVariable("id") long id);
}
