package com.clear.solutions.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {AgeValidator.class})
@Target({TYPE_USE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Age {
    String message() default "Your age does not meet the requirements!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
