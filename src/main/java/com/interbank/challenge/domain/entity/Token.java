package com.interbank.challenge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Token {
    private String accessToken;
    private String scope;
    private Integer expiresIn;
    private String tokenType;

}
