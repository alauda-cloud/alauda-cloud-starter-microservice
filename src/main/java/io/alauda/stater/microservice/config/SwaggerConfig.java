package io.alauda.stater.microservice.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaodawei on 2017/3/27.
 */
@ConditionalOnMissingBean(SwaggerConfig.class)
@EnableOAuth2Client
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${swagger.oauth.base-url}")
    private String oAuthBaseUri;

    @Value("${swagger.oauth.client-id}")
    private String clientId;

    @Value("${swagger.oauth.client-secret}")
    private String clientSecret;

    @Value("${swagger.service-name}")
    private String serviceName;

    @Value("${swagger.service-desc}")
    private String serviceDesc;

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(oAuth2()))
                .securityContexts(Arrays.asList(securityContext()))
                .useDefaultResponseMessages(false);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(serviceName)
                .description(serviceDesc)
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope readScope = new AuthorizationScope("read_scope", "读取资源");
        AuthorizationScope writeScope = new AuthorizationScope("write_scope", "写入资源");
        AuthorizationScope adminScope = new AuthorizationScope("admin_scope", "管理资源");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
        authorizationScopes[0] = readScope;
        authorizationScopes[1] = writeScope;
        authorizationScopes[2] = adminScope;
        return Arrays.asList(new SecurityReference("oauth2", authorizationScopes));
    }

    private List<AuthorizationScope> scopes(){
        return Arrays.asList(
                new AuthorizationScope("read_scope","读取资源"),
                new AuthorizationScope("write_scope","写入资源"),
                new AuthorizationScope("admin_scope","管理资源"));
    }

    private SecurityScheme oAuth2(){

        String TOKEN_REQUEST_URL = oAuthBaseUri+"/oauth/authorize";
        String TOKEN_URL = oAuthBaseUri+"/oauth/token";

        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(TOKEN_URL, "access_token"))
                .tokenRequestEndpoint(new TokenRequestEndpoint(TOKEN_REQUEST_URL, clientId, clientSecret ))
                .build();

        SecurityScheme oauth = new OAuthBuilder().name("oauth2")
                .grantTypes(Arrays.asList(grantType))
                .scopes(scopes())
                .build();

        return oauth;
    }
}
