package edu.examples.todos.presentation.api.security.authentication.customizers;


import org.springframework.security.config.web.server.ServerHttpSecurity;

public interface AuthenticationBuilderCustomizer
{
    ServerHttpSecurity customizeAuthenticationBuilder(ServerHttpSecurity httpSecurity);
}
