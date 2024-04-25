package com.clear.solutions.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@jakarta.validation.constraints.Email(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
public @interface Email {
    String message() default "Invalid email address!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
