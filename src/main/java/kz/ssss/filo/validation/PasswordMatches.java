package kz.ssss.filo.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kz.ssss.filo.validation.validator.PasswordMatcherValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PasswordMatcherValidator.class)
@Target(value = TYPE)
@Retention(RUNTIME)
public @interface PasswordMatches {
    String message() default "Passwords do not match!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
