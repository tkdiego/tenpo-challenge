package com.taka.tenpo.domain.security.service;


import com.taka.tenpo.domain.security.model.TokenResponse;
import com.taka.tenpo.domain.security.repository.SessionRepository;
import com.taka.tenpo.domain.security.util.AuthenticationResolver;
import com.taka.tenpo.domain.security.util.JwtGenerator;
import com.taka.tenpo.domain.security.util.JwtResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.taka.tenpo.domain.security.util.JwtResolver.isValid;
import static com.taka.tenpo.domain.security.util.ParameterConstant.TOKEN_BEARER;


@Service
public class JwtService {

    private final SessionRepository sessionRepository;

    private final JwtGenerator jwtGenerator;

    private final String jwtSignKey;

    public JwtService(SessionRepository sessionRepository, JwtGenerator jwtGenerator, @Value("${jwt.sign.key}") String jwtSignKey) {
        this.sessionRepository = sessionRepository;
        this.jwtGenerator = jwtGenerator;
        this.jwtSignKey = jwtSignKey;
    }

    public TokenResponse generateTokenResponse() {
        String token = jwtGenerator.generate(AuthenticationResolver.getUsername(), jwtSignKey);
        return new TokenResponse(TOKEN_BEARER, token);
    }

    public boolean isValidToken(String token) {
        return isValid(token, jwtSignKey);
    }

    public String getUsername(String token) {
        return JwtResolver.getUsername(token, jwtSignKey);
    }

}
