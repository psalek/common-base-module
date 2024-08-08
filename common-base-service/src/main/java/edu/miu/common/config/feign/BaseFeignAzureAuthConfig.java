package edu.miu.common.config.feign;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Configuration
@ConditionalOnProperty(value = "common.feign-oauth.enabled", havingValue = "true", matchIfMissing = true)
public abstract class BaseFeignAzureAuthConfig {

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    RequestInterceptor requestInterceptor(CachedTokenResponseClient cachedTokenResponseClient) {
        return requestTemplate -> {
            OAuth2AccessToken accessToken = cachedTokenResponseClient.getAccessToken(getClientRegistrationId());
            requestTemplate.header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken.getTokenValue());
        };
    }

    // These method will be provided by the subclasses
    protected abstract String getClientRegistrationId();

}