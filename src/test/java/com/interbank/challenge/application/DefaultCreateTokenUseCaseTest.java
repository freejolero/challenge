package com.interbank.challenge.application;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.domain.port.out.CreateTokenRest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCreateTokenUseCaseTest {

    @Mock
    CreateTokenRest createTokenRest;

    @InjectMocks
    DefaultCreateTokenUseCase createTokenUseCase;

    @Test
    void givenRequest_whenApply_thenReturnToken() {
        when(createTokenRest.apply()).thenReturn(new Token());

        Token token = createTokenUseCase.apply();

        assertThat(token).isNotNull();
    }
}