package com.taka.tenpo.domain.security.model;

import com.taka.tenpo.domain.security.validator.UserPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@UserPassword
public class SignInRequest extends LoginRequest {

    private String confirmPassword;
}
