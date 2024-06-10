package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER
})
@Constraint(validatedBy = ClientSecretValidator.class)
@Documented
public @interface ClientSecret
{
    String message() default "Client secret has not met security requirements";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
