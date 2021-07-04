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
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {

    String message() default "Username can only contain alphanumeric characters";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
