package com.walmart.feeds.api;

import com.walmart.feeds.api.client.tags.TagAdmimCollectionClient;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Created by r0i001q on 19/07/17.
 */
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.walmart.feeds.*")
public class Application {

    @Value("${tagadmin.username}")
    private String tagAdminUser;

    @Value("${tagadmin.password}")
    private String tagAdminPassword;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Create a locale resolver to
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean
    public TagAdmimCollectionClient tagAdmimCollectionClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(TagAdmimCollectionClient.class))
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(new BasicAuthRequestInterceptor(tagAdminUser, tagAdminPassword))
                .target(TagAdmimCollectionClient.class, "http://vip-cat-serv.qa.vmcommerce.intra/ws/");
    }
}
