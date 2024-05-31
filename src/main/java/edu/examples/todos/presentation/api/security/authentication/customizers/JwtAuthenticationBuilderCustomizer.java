package edu.examples.todos.presentation.api.security.authentication.customizers;

import edu.examples.todos.presentation.api.security.authentication.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationBuilderCustomizer implements AuthenticationBuilderCustomizer
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public HttpSecurity customizeAuthenticationBuilder(HttpSecurity httpSecurity)
    {
        return
                httpSecurity
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
