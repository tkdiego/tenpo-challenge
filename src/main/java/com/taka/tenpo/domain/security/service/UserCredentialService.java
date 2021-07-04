package com.taka.tenpo.domain.security.service;

import com.taka.tenpo.domain.security.exception.InvalidUsernameException;
import com.taka.tenpo.domain.security.model.UserCredential;
import com.taka.tenpo.domain.security.model.UserData;
import com.taka.tenpo.domain.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.taka.tenpo.domain.security.util.AuthenticationResolver.generateInvalidCredential;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@AllArgsConstructor
public class UserCredentialService implements UserDetailsService {

    private static final Logger LOGGER = getLogger(UserCredentialService.class);

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userRepository.findByUsername(username);
        if (user != null) {
            return UserCredential.build(user);
        }
        generateInvalidCredential(username);
        String msg = format("Username: %s is not registered", username);
        LOGGER.info(msg);
        throw new InvalidUsernameException(msg);
    }
}
