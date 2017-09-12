package com.walmart.feeds.api;

import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import com.walmart.feeds.api.core.service.feed.ProductCollectionServiceImpl;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TagAdminConfiguration {

	@Value("${tagadmin.username}")
	private String tagAdminUser;

	@Value("${tagadmin.password}")
	private String tagAdminPassword;

	@Value("${tagadmin.uri}")
	private String uri;

	@Bean
	public TagAdmimCollectionClient tagAdmimCollectionClient() {
		return Feign.builder()
				.client(new OkHttpClient())
				.decoder(new JacksonDecoder())
				.errorDecoder(new ProductCollectionServiceImpl())
				.logger(new Slf4jLogger(TagAdmimCollectionClient.class))
				.logLevel(Logger.Level.FULL)
				.requestInterceptor(new BasicAuthRequestInterceptor(tagAdminUser, tagAdminPassword))
				.target(TagAdmimCollectionClient.class, uri);
	}
}
