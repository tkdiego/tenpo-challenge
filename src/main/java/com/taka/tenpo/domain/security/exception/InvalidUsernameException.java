package com.taka.tenpo.domain.security.exception;

public class InvalidUsernameException extends IllegalArgumentException {

    public InvalidUsernameException(String format) {
        super(format);
    }
}
