package io.alauda.stater.microservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnMissingBean(ResourceServerConfig.class)
@Import({TokenConfig.class,JwtFilterConfig.class,SwaggerConfig.class})
@EnableResourceServer
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()

                .antMatchers(
                        "/health",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security"
                ).permitAll()
                .anyRequest().authenticated();
    }
}
