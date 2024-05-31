package edu.examples.todos.presentation.api.security.config;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Value("${application.security.authentication.methods.basic.realm-name}")
public @interface HttpBasicRealmValue
{
}
