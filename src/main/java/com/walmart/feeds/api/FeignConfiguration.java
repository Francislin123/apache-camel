package com.walmart.feeds.api;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vn0gshm on 25/08/17.
 */
@EnableFeignClients
@Configuration
public class FeignConfiguration {

    @Value("${tagadmin.username}")
    private String tagAdminUser;

    @Value("${tagadmin.password}")
    private String tagAdminPassword;

    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(tagAdminUser, tagAdminPassword);
    }
}
