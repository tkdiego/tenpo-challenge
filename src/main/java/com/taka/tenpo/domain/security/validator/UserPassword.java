package com.taka.tenpo.domain.security.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface UserPassword {

    String message() default "Password and password confirmation must not be null and must be the same.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
