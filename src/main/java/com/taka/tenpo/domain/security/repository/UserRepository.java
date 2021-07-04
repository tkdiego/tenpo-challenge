package com.taka.tenpo.domain.security.repository;

import com.taka.tenpo.domain.security.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {

    public UserData findByUsername(String username);

    public boolean existsUserByUsername(String username);

}
