package com.taka.tenpo.domain.security.filter;


import com.taka.tenpo.domain.security.service.ISessionSecurityService;
import com.taka.tenpo.domain.security.service.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.taka.tenpo.domain.security.service.AuthenticationService.generateCredential;
import static com.taka.tenpo.domain.security.service.JwtService.getTokenFromHeader;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@AllArgsConstructor
public class TokenInspectorFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = getLogger(TokenInspectorFilter.class);

    private UserDetailsService userDetailsService;

    private JwtService jwtService;

    private ISessionSecurityService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ofNullable(getTokenFromHeader(httpServletRequest)).ifPresent(token ->
                verifyAndAuthorizeAccess(httpServletRequest, token));
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void verifyAndAuthorizeAccess(HttpServletRequest httpServletRequest, String token) {
        if (jwtService.isValidToken(token) && sessionService.existSessionByToken(token)) {
            generateCredential(httpServletRequest, sessionService.getUserData(jwtService.getUsername(token)));
        } else {
            LOGGER.error("The token has already expired. Username: {}.", jwtService.getUsername(token));
        }
    }

}
