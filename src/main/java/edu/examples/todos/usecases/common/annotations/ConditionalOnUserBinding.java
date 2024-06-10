package edu.examples.todos.usecases.common.annotations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@ConditionalOnProperty(name = "application.use-cases.binding", havingValue = "users")
@Primary
public @interface ConditionalOnUserBinding
{
}
