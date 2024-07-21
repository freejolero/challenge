package com.interbank.challenge.application;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.domain.port.in.CreateTokenUseCase;
import com.interbank.challenge.domain.port.out.CreateTokenRest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCreateTokenUseCase implements CreateTokenUseCase {

    private final CreateTokenRest createTokenRest;

    @Override
    public Token apply() {
        return createTokenRest.apply();
    }
}
