package com.taka.tenpo.domain.security.repository;

import com.taka.tenpo.domain.security.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {

    UserData findByUsername(String username);

    boolean existsUserByUsername(String username);

}
