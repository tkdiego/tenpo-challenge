package com.taka.tenpo.domain.security.service;


import com.taka.tenpo.domain.security.model.TokenResponse;
import com.taka.tenpo.domain.security.repository.SessionRepository;
import com.taka.tenpo.domain.security.util.AuthenticationResolver;
import com.taka.tenpo.domain.security.util.JwtGenerator;
import com.taka.tenpo.domain.security.util.JwtTranslator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.taka.tenpo.domain.security.util.JwtTranslator.isValid;
import static com.taka.tenpo.domain.security.util.ParameterConstant.START_TOKEN_INDEX;
import static com.taka.tenpo.domain.security.util.ParameterConstant.TOKEN_BEARER;
import static com.taka.tenpo.domain.security.util.ParameterConstant.TOKEN_HEADER;
import static org.springframework.util.StringUtils.hasText;


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

    public static String getTokenFromHeader(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        return removeBearerToToken(tokenHeader);
    }

    private static String removeBearerToToken(String token) {
        if (containBearer(token)) {
            return token.substring(START_TOKEN_INDEX);
        }
        return token;
    }

    private static boolean containBearer(String token) {
        return hasText(token) && token.startsWith(TOKEN_BEARER);
    }

    public TokenResponse generateTokenResponse() {
        String token = jwtGenerator.generate(AuthenticationResolver.getUsername(), jwtSignKey);
        return new TokenResponse(TOKEN_BEARER, token);
    }

    public boolean isValidToken(String token) {
        if (containBearer(token)) {
            token = removeBearerToToken(token);
        }
        return isValid(token, jwtSignKey);
    }

    public String getUsername(String token) {
        if (containBearer(token)) {
            token = removeBearerToToken(token);
        }
        return JwtTranslator.getUsername(token, jwtSignKey);
    }

}
