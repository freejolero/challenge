package com.interbank.challenge.infrastructure.restclient.adapter.mapper;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.infrastructure.restclient.adapter.dto.TokenResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {

    public Token mapToToken(TokenResponse tokenResponse) {
        Token token = new Token();
        token.setAccessToken(tokenResponse.getAccessToken());
        token.setScope(tokenResponse.getScope());
        token.setExpiresIn(tokenResponse.getExpiresIn());
        token.setTokenType(tokenResponse.getTokenType());
        return token;
    }
}
