package com.taka.tenpo.domain.security.repository;

import com.taka.tenpo.domain.security.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionData, Long> {

    boolean existsByToken(String token);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    SessionData getByUsername(String username);

}
