package com.taka.tenpo.domain.security.service;

import com.taka.tenpo.domain.security.model.LoginRequest;
import com.taka.tenpo.domain.security.model.LogoutRequest;
import com.taka.tenpo.domain.security.model.SignInRequest;
import com.taka.tenpo.domain.security.model.TokenResponse;

public interface ISessionService {

    TokenResponse login(LoginRequest loginRequest);

    void logout(LogoutRequest logoutRequest);

    void signIn(SignInRequest signInRequest);
}
