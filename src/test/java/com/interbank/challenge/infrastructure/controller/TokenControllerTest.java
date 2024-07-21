package com.interbank.challenge.infrastructure.controller;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.domain.port.in.CreateTokenUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @Mock
    CreateTokenUseCase createTokenUseCase;

    @InjectMocks
    TokenController tokenController;

    @Test
    void givenRequest_whenGetToken_thenReturnOk() {
        when(createTokenUseCase.apply()).thenReturn(new Token());

        ResponseEntity<Void> responseEntity = tokenController.getEmployeeById();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }


}