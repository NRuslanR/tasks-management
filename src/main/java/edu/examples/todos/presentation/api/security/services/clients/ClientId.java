package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER
})
@Constraint(validatedBy = ClientIdValidator.class)
@Documented
public @interface ClientId
{
    String message() default "Client id has not met requirements";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
