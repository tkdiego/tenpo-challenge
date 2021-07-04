package com.taka.tenpo.domain.security.validator;

import com.taka.tenpo.domain.security.model.SignInRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.hasText;


public class PasswordValidator implements ConstraintValidator<UserPassword, SignInRequest> {

    @Override
    public boolean isValid(SignInRequest signInRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(!hasText(signInRequest.getPassword()) || !hasText(signInRequest.getConfirmPassword())){
            return false;
        }
        return signInRequest.getPassword().equals(signInRequest.getConfirmPassword());
    }
}