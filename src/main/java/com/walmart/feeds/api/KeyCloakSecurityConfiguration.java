package com.walmart.feeds.api;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.walmart.feeds.api.core.utils.FeedsAdminAPIRoles.*;

@KeycloakConfiguration
public class KeyCloakSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    public static final String API_URL_MATCHER = "/v1/**";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
                    .addFilterBefore(keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests()
                //actuator endpoints
                .antMatchers("/health", "/healthcheck", "/healthcheck/complete", "/info").permitAll()
                .antMatchers("/metrics", "/dump", "/loggers", "/loggers/**", "/trace", "/autoconfig", "/mappings", "/configprops", "/heapdump", "/env", "/env/**", "/beans", "/auditevents").hasRole(FEEDS_ADMIN)
                //swagger endpoints
                .antMatchers("/configuration/ui", "/configuration/security", "/v2/api-docs", "/docs/index.html", "/swagger-resources/**", "/webjars/**", "/swagger-ui.html", "/swagger-ui.html#/**").permitAll()
                .antMatchers(HttpMethod.GET, API_URL_MATCHER).hasAnyRole(FEEDS_READ_ONLY, FEEDS_OPERATOR, FEEDS_MANAGER, FEEDS_ADMIN)
                .antMatchers(HttpMethod.POST, API_URL_MATCHER).hasAnyRole(FEEDS_OPERATOR, FEEDS_MANAGER, FEEDS_ADMIN)
                .antMatchers(HttpMethod.PUT, API_URL_MATCHER).hasAnyRole(FEEDS_OPERATOR, FEEDS_MANAGER, FEEDS_ADMIN)
                .antMatchers(HttpMethod.PATCH, API_URL_MATCHER).hasAnyRole(FEEDS_OPERATOR, FEEDS_MANAGER, FEEDS_ADMIN)
                .antMatchers(HttpMethod.DELETE, API_URL_MATCHER).hasAnyRole(FEEDS_MANAGER, FEEDS_ADMIN)
            .anyRequest().authenticated();
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}