package com.taka.tenpo.domain.security.exception;

public class UsernameNotAvailableException extends IllegalArgumentException {

    public UsernameNotAvailableException(String message) {
        super(message);
    }
}
