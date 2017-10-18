package com.walmart.feeds.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Created by r0i001q on 19/07/17.
 */
@SpringBootApplication(scanBasePackages = "com.walmart.feeds.*")
@EnableJpaAuditing
@EnableCaching
public class FeedsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedsAdminApplication.class, args);
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
}
