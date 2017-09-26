package com.walmart.feeds.api;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	private static final String API_KEY = "api_key";
	private static final String SWAGGER_KEYCLOAK_AUTH = "swagger_keyacloak_auth";

	@Value("${wm.swagger.keycloak.authentication-url}")
	private String authenticationUrl;

	@Value("${wm.swagger.keycloak.token-url}")
	private String tokenUrl;

	@Value("${wm.swagger.keycloak.client.id}")
	private String clientId;

	@Value("${wm.swagger.keycloak.client.credentials.secret}")
	private String secretKey;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${wm.swagger.keycloak.client.scope.name}")
	private String scopeName;

	@Value("${wm.swagger.keycloak.client.scope.description}")
	private String scopeDescription;

	@Bean
	public Docket feedsAdminApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder()
							.contact(new Contact("Feeds Team", null, "gecbrfeeds@walmart.com"))
							.title("Feeds Admin Api")
							.version("v1.0")
							.description("Application to configure feeds and manage execution of jobs to generate feed files")
						.build())
				.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.regex(".*/v\\d+/.*"))
				.build()
				.securitySchemes(Collections.singletonList(oauths()))
				.securityContexts(Collections.singletonList(securityContext()));
	}

	@Bean
	SecurityScheme oauths() {
		return new OAuthBuilder().name(SWAGGER_KEYCLOAK_AUTH).grantTypes(grantTypes()).scopes(scopes()).build();
	}

	@Bean
	public SecurityScheme apiKey() {
		return new ApiKey(API_KEY, HttpHeaders.AUTHORIZATION, ApiKeyVehicle.HEADER.getValue());// header ,query two values allowed
	}

	List<AuthorizationScope> scopes() {
		return Arrays.asList(new AuthorizationScope(scopeName, scopeDescription));
	}

	List<GrantType> grantTypes() {
		return Arrays.asList(new AuthorizationCodeGrantBuilder()
				.tokenEndpoint(new TokenEndpointBuilder()
						.tokenName("keycloak-token")
						.url(tokenUrl)
					.build())
				.tokenRequestEndpoint(new TokenRequestEndpointBuilder()
						.clientIdName("client-id")
						.clientSecretName("secret-key")
						.url(authenticationUrl)
					.build())
			.build());
	}

	@Bean
	public SecurityConfiguration security() {
		return new SecurityConfiguration(clientId, secretKey, realm, "feeds-admin-api-swagger", null, ApiKeyVehicle.HEADER, API_KEY, null);
	}

	@Bean
	public SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("/*.*"))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		final AuthorizationScope authorizationScope
				= new AuthorizationScope(scopeName, scopeDescription);
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference(API_KEY, authorizationScopes));
	}

}
