package com.interbank.challenge.infrastructure.restclient.adapter;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.infrastructure.controller.exception.ProviderException;
import com.interbank.challenge.infrastructure.restclient.adapter.config.TokenRequestConfig;
import com.interbank.challenge.infrastructure.restclient.adapter.dto.TokenResponse;
import com.interbank.challenge.infrastructure.restclient.adapter.mapper.TokenMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTokenAdapterTest {

    @Mock
    WebClient.Builder webClientBuilder;
    @Mock
    WebClient client;
    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    WebClient.RequestBodySpec requestBodySpec;
    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    WebClient.ResponseSpec responseSpec;
    @Mock
    CustomMinimalForTestResponseSpec responseSpecMock;

    CreateTokenAdapter createTokenAdapter;

    @BeforeEach
    void setUp() {
        TokenRequestConfig tokenRequestConfig = new TokenRequestConfig();
        tokenRequestConfig.setUrl("https://dev-trials.us.auth0.com");
        tokenRequestConfig.setClientId("JaYsfjv6AkWaspRaibS1zQy5RBpyhCkE");
        tokenRequestConfig.setClientSecret("jJ73udoXij1W0fGMcOAi6k76SuC1zTTqevthYFxsHZm6e_ESk7xdQxIiuT91PvAN");
        tokenRequestConfig.setAudience("https://dev-trials.us.auth0.com/api/v2/");
        tokenRequestConfig.setGrantType("client_credentials");
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(client);
        createTokenAdapter = new CreateTokenAdapter(tokenRequestConfig, new TokenMapper(), webClientBuilder);
    }

    @Test
    void givenRequest_whenApply_thenReturnToken() {
        TokenResponse tokenMono = new TokenResponse();
        tokenMono.setAccessToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1qQkVSRGd6T1RZMk");
        tokenMono.setScope("read:client_grants create:client_grants delete:client_grants");
        tokenMono.setTokenType("Bearer");
        tokenMono.setExpiresIn(86400);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TokenResponse.class)).thenReturn(Mono.just(tokenMono));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(requestBodySpec.body(any(), eq(TokenRequestConfig.class))).thenReturn(requestHeadersSpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(client.post()).thenReturn(requestBodyUriSpec);
        Token tokenResponse = createTokenAdapter.apply();

        assertThat(tokenResponse).isNotNull().satisfies(token -> {
            assertThat(token.getAccessToken()).isEqualTo(tokenMono.getAccessToken());
            assertThat(token.getScope()).isEqualTo(tokenMono.getScope());
            assertThat(token.getTokenType()).isEqualTo(tokenMono.getTokenType());
            assertThat(token.getExpiresIn()).isEqualTo(tokenMono.getExpiresIn());
        });
    }

    @Test
    void givenRequest_whenStatus4xx_thenThrowException() {
        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(responseSpecMock.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpecMock);
        when(requestBodySpec.body(any(), eq(TokenRequestConfig.class))).thenReturn(requestHeadersSpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(client.post()).thenReturn(requestBodyUriSpec);
        assertThatThrownBy(() -> createTokenAdapter.apply())
                .isInstanceOf(ProviderException.class)
                .hasMessage("Unauthorized")
                .extracting("errorCode", InstanceOfAssertFactories.type(String.class))
                .satisfies(errorCode -> {
                    assertThat(errorCode).isEqualTo("PR-001");
                });
    }

    @Test
    void givenRequest_whenStatus5xx_thenThrowException() {
        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(responseSpecMock.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpecMock);
        when(requestBodySpec.body(any(), eq(TokenRequestConfig.class))).thenReturn(requestHeadersSpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(client.post()).thenReturn(requestBodyUriSpec);
        assertThatThrownBy(() -> createTokenAdapter.apply())
                .isInstanceOf(ProviderException.class)
                .hasMessage("Internal Server Error")
                .extracting("errorCode", InstanceOfAssertFactories.type(String.class))
                .satisfies(errorCode -> {
                    assertThat(errorCode).isEqualTo("PR-002");
                });
    }

    abstract class CustomMinimalForTestResponseSpec implements WebClient.ResponseSpec {

        public abstract HttpStatus getStatus();

        public WebClient.ResponseSpec onStatus(Predicate<HttpStatus> statusPredicate, Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            if (statusPredicate.test(this.getStatus())) exceptionFunction.apply(ClientResponse.create(this.getStatus()).build()).block();
            return this;
        }

    }
}