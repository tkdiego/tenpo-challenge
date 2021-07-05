package com.taka.tenpo.domain.security.service;

import com.taka.tenpo.domain.security.model.UserData;

public interface ISessionSecurityService {

    boolean existSessionByToken(String token);

    UserData getUserData(String username);
}
