package com.taka.tenpo.domain.security.validator;

import com.taka.tenpo.domain.security.model.LoginRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.hasText;

public class UsernameValidator implements ConstraintValidator<Username, LoginRequest> {

    private static final String PATTERN = "[A-Za-z0-9]+";

    private static final int CHARACTER_LIMIT_LENGTH = 30;

    @Override
    public boolean isValid(LoginRequest loginRequest, ConstraintValidatorContext constraintValidatorContext) {
        return hasText(loginRequest.getUsername()) && loginRequest.getUsername().matches(PATTERN)
                && loginRequest.getUsername().length() <= CHARACTER_LIMIT_LENGTH;
    }
}
