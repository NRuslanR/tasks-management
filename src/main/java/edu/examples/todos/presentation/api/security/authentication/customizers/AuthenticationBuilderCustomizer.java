package edu.examples.todos.presentation.api.security.authentication.customizers;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface AuthenticationBuilderCustomizer
{
    HttpSecurity customizeAuthenticationBuilder(HttpSecurity httpSecurity);
}
