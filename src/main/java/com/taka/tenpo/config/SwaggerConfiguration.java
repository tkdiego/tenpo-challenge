package com.taka.tenpo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.taka.tenpo.config.SwaggerData.API_INFO_CONTACT_MAIL;
import static com.taka.tenpo.config.SwaggerData.API_INFO_CONTACT_NAME;
import static com.taka.tenpo.config.SwaggerData.API_INFO_CONTACT_URL;
import static com.taka.tenpo.config.SwaggerData.API_INFO_DESCRIPTION;
import static com.taka.tenpo.config.SwaggerData.API_INFO_LICENSE;
import static com.taka.tenpo.config.SwaggerData.API_INFO_LICENSE_URL;
import static com.taka.tenpo.config.SwaggerData.API_INFO_TERMS_URL;
import static com.taka.tenpo.config.SwaggerData.API_INFO_TITLE;
import static com.taka.tenpo.config.SwaggerData.API_INFO_VERSION;
import static com.taka.tenpo.config.SwaggerData.API_KEY_NAME;
import static com.taka.tenpo.config.SwaggerData.API_NAME;
import static com.taka.tenpo.config.SwaggerData.API_PASS_AS;
import static com.taka.tenpo.config.SwaggerData.AUTHORIZATION_SCOPE_DESCRIPTION;
import static com.taka.tenpo.config.SwaggerData.AUTHORIZATION_SCOPE_TYPE;
import static com.taka.tenpo.config.SwaggerData.BASE_PACKAGE;
import static com.taka.tenpo.config.SwaggerData.SECURITY_REFERENCE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .apiInfo(getApiInfo())
                .securityContexts(singletonList(getSecurityContext()))
                .securitySchemes(singletonList(getApiKey()))
                .select()
                .apis(basePackage(BASE_PACKAGE))
                .paths(any())
                .build();
    }

    private static SecurityContext getSecurityContext() {
        return SecurityContext.builder().securityReferences(getAuthorization()).build();
    }

    private static List<SecurityReference> getAuthorization() {
        AuthorizationScope authorizationScope = new AuthorizationScope(AUTHORIZATION_SCOPE_TYPE, AUTHORIZATION_SCOPE_DESCRIPTION);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(new SecurityReference(SECURITY_REFERENCE, authorizationScopes));
    }

    private static ApiInfo getApiInfo() {
        return new ApiInfo(
                API_INFO_TITLE,
                API_INFO_DESCRIPTION,
                API_INFO_VERSION,
                API_INFO_TERMS_URL,
                new Contact(API_INFO_CONTACT_NAME, API_INFO_CONTACT_URL, API_INFO_CONTACT_MAIL),
                API_INFO_LICENSE,
                API_INFO_LICENSE_URL,
                emptyList());
    }

    private static ApiKey getApiKey() {
        return new ApiKey(API_NAME, API_KEY_NAME, API_PASS_AS);
    }
}
