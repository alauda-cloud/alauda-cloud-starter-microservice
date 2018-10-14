package io.alauda.stater.microservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingBean(JwtFilterConfig.class)
@ServletComponentScan("io.alauda.starter.microserivce.filter")
@Configuration
public class JwtFilterConfig {
}
