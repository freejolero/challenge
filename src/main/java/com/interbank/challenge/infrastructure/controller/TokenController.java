package com.interbank.challenge.infrastructure.controller;

import com.interbank.challenge.domain.entity.Token;
import com.interbank.challenge.domain.port.in.CreateTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final CreateTokenUseCase createTokenUseCase;

    @PostMapping
    public ResponseEntity<Void> getEmployeeById() {
        Token token = createTokenUseCase.apply();
        log.info("Token: {}", token.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
