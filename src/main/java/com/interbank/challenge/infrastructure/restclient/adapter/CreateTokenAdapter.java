package com.interbank.challenge.infrastructure.restclient.adapter;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.domain.port.out.CreateTokenRest;
import com.interbank.challenge.infrastructure.controller.exception.ProviderException;
import com.interbank.challenge.infrastructure.restclient.adapter.config.TokenRequestConfig;
import com.interbank.challenge.infrastructure.restclient.adapter.dto.TokenResponse;
import com.interbank.challenge.infrastructure.restclient.adapter.mapper.TokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CreateTokenAdapter implements CreateTokenRest {

    private final TokenRequestConfig tokenRequestConfig;
    private final TokenMapper tokenMapper;
    private final WebClient client;

    public CreateTokenAdapter(TokenRequestConfig tokenRequestConfig, TokenMapper tokenMapper, WebClient.Builder webClientBuilder) {
        this.tokenRequestConfig = tokenRequestConfig;
        this.tokenMapper = tokenMapper;
        this.client = webClientBuilder.baseUrl(tokenRequestConfig.getUrl()).build();
    }

    @Override
    public Token apply() {

        Mono<TokenResponse> tokenMono = client.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tokenRequestConfig), TokenRequestConfig.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ProviderException("PR-001", clientResponse.statusCode().getReasonPhrase())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new ProviderException("PR-002", clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(TokenResponse.class);

        return tokenMono.blockOptional().map(tokenMapper::mapToToken).orElse(new Token());
    }
}
