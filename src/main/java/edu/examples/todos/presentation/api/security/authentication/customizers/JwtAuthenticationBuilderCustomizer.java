package edu.examples.todos.presentation.api.security.authentication.customizers;

import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import edu.examples.todos.presentation.api.security.authentication.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationBuilderCustomizer implements AuthenticationBuilderCustomizer
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ServerHttpSecurity customizeAuthenticationBuilder(ServerHttpSecurity httpSecurity)
    {
        return
                httpSecurity
                        .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);
    }
}
