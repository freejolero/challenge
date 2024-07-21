package com.interbank.challenge.infrastructure.restclient.adapter.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("web.client.token")
public class TokenRequestConfig {
    private String url;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String audience;
    @JsonProperty("grant_type")
    private String grantType;
}
