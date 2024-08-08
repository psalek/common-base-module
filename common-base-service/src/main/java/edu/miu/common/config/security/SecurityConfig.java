package edu.miu.common.config.security;

import edu.miu.common.config.properties.CommonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer.aadResourceServer;
import static com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer.aadWebApplication;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "common.security.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {
    @Autowired
    private CommonProperties properties;

    private static final String API_ROLE = "APPROLE_API";

    @Bean
    @Order(1)
    @ConditionalOnProperty(value = "common.security.api.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        log.debug("initializing default api security filter chain");

        // @formatter:off
        http.with(aadResourceServer(), Customizer.withDefaults())
            .securityMatcher(properties.getSecurity().getApi().getPathPrefix() + "/**")
            .authorizeHttpRequests(request -> request
                    .anyRequest().hasAnyAuthority(API_ROLE));
        // @formatter:on

        return http.build();
    }

    @Bean
    @Order(2)
    @ConditionalOnProperty(value = "common.security.hmi.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain hmiSecurityFilterChain(HttpSecurity http) throws Exception {
        log.debug("initializing default hmi security filter chain");

        // @formatter:off
        http.with(aadWebApplication(), Customizer.withDefaults())
            .securityMatcher(properties.getSecurity().getHmi().getPathPrefix() + "/**")
            .authorizeHttpRequests(request -> request
                    .anyRequest().authenticated());
        // @formatter:on

        return http.build();
    }
}
