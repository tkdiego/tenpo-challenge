package com.taka.tenpo.domain.security.service;

import com.taka.tenpo.domain.security.exception.UsernameNotAvailableException;
import com.taka.tenpo.domain.security.model.LoginRequest;
import com.taka.tenpo.domain.security.model.LogoutRequest;
import com.taka.tenpo.domain.security.model.SessionData;
import com.taka.tenpo.domain.security.model.SignInRequest;
import com.taka.tenpo.domain.security.model.TokenResponse;
import com.taka.tenpo.domain.security.model.UserData;
import com.taka.tenpo.domain.security.repository.SessionRepository;
import com.taka.tenpo.domain.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.taka.tenpo.domain.security.service.AuthenticationService.generateCredential;
import static com.taka.tenpo.domain.security.service.AuthenticationService.getUserDetails;
import static com.taka.tenpo.domain.security.service.AuthenticationService.getUserId;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@AllArgsConstructor
public class SessionService implements ISessionService, ISessionSecurityService {

    private static final Logger LOGGER = getLogger(SessionService.class);

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationService authenticationResolver;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationResolver.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        TokenResponse tokenResponse = jwtService.generateTokenResponse();
        if (!sessionRepository.existsByUsername(loginRequest.getUsername())) {
            sessionRepository.save(new SessionData(getUserId(), loginRequest.getUsername(), tokenResponse.getValue()));
            LOGGER.info("The {}'s session was created. Login was successful.", loginRequest.getUsername());
        } else {
            LOGGER.info("Username {} is already logged in. Returns the new token.", loginRequest.getUsername());
            updateSessionInformation(loginRequest, tokenResponse);
        }
        return tokenResponse;
    }

    @Override
    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        String username = jwtService.getUsername(logoutRequest.getToken());
        sessionRepository.deleteByUsername(username);
        getUserDetails().ifPresent(userDetails -> userDetails.setCredentialsNonExpired(false));
        LOGGER.info("Username {} logged out successfully", username);
    }

    @Override
    @Transactional
    public void signIn(SignInRequest signInRequest) {
        if (!userRepository.existsUserByUsername(signInRequest.getUsername())) {
            saveAndCreateSession(signInRequest);
            LOGGER.info("Username {} was created.", signInRequest.getUsername());
        } else {
            String msg = format("Username %s already exists.", signInRequest.getUsername());
            LOGGER.info(msg);
            throw new UsernameNotAvailableException(msg);
        }
    }

    @Override
    public boolean existSessionByToken(String token) {
        return sessionRepository.existsByToken(token);
    }

    @Override
    public UserData getUserData(String username) {
        return userRepository.findByUsername(username);
    }

    private void updateSessionInformation(LoginRequest loginRequest, TokenResponse tokenResponse) {
        SessionData currentSessionData = sessionRepository.getByUsername(loginRequest.getUsername());
        currentSessionData.setToken(tokenResponse.getValue());
        sessionRepository.save(currentSessionData);
    }

    private void saveAndCreateSession(SignInRequest signInRequest) {
        UserData newUser = new UserData(signInRequest.getUsername(), passwordEncoder.encode(signInRequest.getPassword()));
        UserData userData = userRepository.save(newUser);
        generateCredential(userData);
    }
}
