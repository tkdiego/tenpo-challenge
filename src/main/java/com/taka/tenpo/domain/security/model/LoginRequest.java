package com.taka.tenpo.domain.security.model;

import com.taka.tenpo.domain.security.validator.Username;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Username
public class LoginRequest {

    private String username;

    private String password;

}
