package edu.miu.common.config.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "common.feign-oauth.enabled", havingValue = "true", matchIfMissing = true)
public class CachedTokenResponseClient {

    private final Map<String, OAuth2AccessTokenResponse> tokenResponses = new ConcurrentHashMap<>();

    private final ClientRegistrationRepository clientRegistrationRepository;

    public CachedTokenResponseClient(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public OAuth2AccessToken getAccessToken(String clientRegistrationId) {
        return Optional.ofNullable(clientRegistrationRepository.findByRegistrationId(clientRegistrationId))
                .map(OAuth2ClientCredentialsGrantRequest::new)
                .map(this::getTokenResponse)
                .orElseThrow(() -> new IllegalStateException("Client ID: '" + clientRegistrationId + "' not found"))
                .getAccessToken();
    }

    private OAuth2AccessTokenResponse getTokenResponse(OAuth2ClientCredentialsGrantRequest grantRequest) {
        String registrationId = grantRequest.getClientRegistration().getRegistrationId();
        OAuth2AccessTokenResponse tokenResponse = tokenResponses.get(registrationId);
        if (isInvalidToken(tokenResponse)) {
            synchronized(this) {
                // Double-check inside synchronized block
                tokenResponse = tokenResponses.get(registrationId);
                if (isInvalidToken(tokenResponse)) {
                    DefaultClientCredentialsTokenResponseClient tokenResponseClient = new DefaultClientCredentialsTokenResponseClient();
                    tokenResponse = tokenResponseClient.getTokenResponse(grantRequest);
                    tokenResponses.put(registrationId, tokenResponse);
                }
            }
        }
        return tokenResponse;
    }

    private boolean isInvalidToken(OAuth2AccessTokenResponse tokenResponse) {
        if(Objects.isNull(tokenResponse)) return true;
        if(Objects.isNull(tokenResponse.getAccessToken())) return true;
        if(tokenResponse.getAccessToken().getExpiresAt() == null) return true;

        return tokenResponse.getAccessToken().getExpiresAt().isBefore(Instant.now());
    }
}