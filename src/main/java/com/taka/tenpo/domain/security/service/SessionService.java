package com.taka.tenpo.domain.security.service;

import com.taka.tenpo.domain.security.exception.InvalidUsernameException;
import com.taka.tenpo.domain.security.model.LoginRequest;
import com.taka.tenpo.domain.security.model.LogoutRequest;
import com.taka.tenpo.domain.security.model.SessionData;
import com.taka.tenpo.domain.security.model.SignInRequest;
import com.taka.tenpo.domain.security.model.TokenResponse;
import com.taka.tenpo.domain.security.model.UserData;
import com.taka.tenpo.domain.security.repository.SessionRepository;
import com.taka.tenpo.domain.security.repository.UserRepository;
import com.taka.tenpo.domain.security.util.AuthenticationResolver;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.taka.tenpo.domain.security.util.AuthenticationResolver.generateCredential;
import static com.taka.tenpo.domain.security.util.AuthenticationResolver.getUserDetails;
import static com.taka.tenpo.domain.security.util.AuthenticationResolver.getUserId;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@AllArgsConstructor
@Setter
public class SessionService {

    private static final Logger LOGGER = getLogger(SessionService.class);

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationResolver authenticationResolver;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationResolver.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        TokenResponse tokenResponse = jwtService.generateTokenResponse();
        if (!sessionRepository.existsByUsername(loginRequest.getUsername())) {
            sessionRepository.save(new SessionData(getUserId(), loginRequest.getUsername(), tokenResponse.getValue()));
            LOGGER.info("The {}'s session was created. Login was successful.", loginRequest.getUsername());
        } else {
            LOGGER.info("Username {} is already logged in. Returns the same token.", loginRequest.getUsername());
            SessionData currentSessionData = sessionRepository.getByUsername(loginRequest.getUsername());
            currentSessionData.setToken(tokenResponse.getValue());
            sessionRepository.save(currentSessionData);
        }
        return tokenResponse;
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        String username = jwtService.getUsername(logoutRequest.getToken());
        sessionRepository.deleteByUsername(username);
        getUserDetails().ifPresent(userDetails -> userDetails.setCredentialsNonExpired(false));
        LOGGER.info("Username {} logged out successfully", username);
    }

    @Transactional
    public void signIn(SignInRequest signInRequest) {
        if (!userRepository.existsUserByUsername(signInRequest.getUsername())) {
            UserData newUser = new UserData(signInRequest.getUsername(), passwordEncoder.encode(signInRequest.getPassword()));
            UserData userData = userRepository.save(newUser);
            generateCredential(userData);
            LOGGER.info("Username {} was created.", signInRequest.getUsername());
        } else {
            String msg = format("Username %s already exists.", signInRequest.getUsername());
            LOGGER.info(msg);
            throw new InvalidUsernameException(msg);
        }
    }

    public boolean existSessionByToken(String token) {
        return sessionRepository.existsByToken(token);
    }

    public UserData getUserData(String username) {
        return userRepository.findByUsername(username);
    }
}
