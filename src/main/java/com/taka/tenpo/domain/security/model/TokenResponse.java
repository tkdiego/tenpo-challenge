package com.taka.tenpo.domain.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class TokenResponse {

    private String key;

    private String value;
}
