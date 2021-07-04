package com.taka.tenpo.domain.security.util;

import com.taka.tenpo.domain.security.model.UserCredential;
import com.taka.tenpo.domain.security.model.UserData;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.taka.tenpo.domain.security.util.ParameterConstant.ANONYMOUS;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

@AllArgsConstructor
public class AuthenticationResolver {

    private static final Logger LOGGER = getLogger(AuthenticationResolver.class);

    private final AuthenticationManager authenticationManager;

    public static String getUsername() {
        return getUserDetails().map(userDetails -> userDetails.getUsername()).orElse(ANONYMOUS);
    }

    public static Long getUserId() {
        return getUserDetails().map(userDetails -> userDetails.getId()).orElse(null);
    }

    public static Optional<UserCredential> getUserDetails() {
        try {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserCredential) {
                return of((UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            }
        } catch (Exception e) {
            LOGGER.error("Security context error.");
        }
        LOGGER.warn("User details could not be obtained. By default it will be anonymous.");
        return empty();
    }

    public static void generateCredential(HttpServletRequest httpServletRequest, UserData userData) {
        UserCredential userCredential = UserCredential.build(userData);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userCredential, null, userCredential.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void generateCredential(UserData userData) {
        UserCredential userCredential = UserCredential.build(userData);
        setCredentialToContext(userCredential);
    }

    public static void generateInvalidCredential(String username) {
        UserCredential userCredential = UserCredential.build(username);
        userCredential.setCredentialsNonExpired(false);
        setCredentialToContext(userCredential);
    }

    public void authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private static void setCredentialToContext(UserCredential userCredential) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userCredential, null, userCredential.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
