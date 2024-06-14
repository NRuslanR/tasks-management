package edu.examples.todos.presentation.api.security.authentication.customizers;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import edu.examples.todos.presentation.api.security.config.HttpBasicRealmValue;
import lombok.SneakyThrows;

public class HttpBasicAuthenticationBuilderCustomizer implements AuthenticationBuilderCustomizer
{
    @HttpBasicRealmValue
    private String realmName;

    @SneakyThrows
    @Override
    public ServerHttpSecurity customizeAuthenticationBuilder(ServerHttpSecurity httpSecurity)
    {
        return
                httpSecurity
                    .httpBasic(c -> Customizer.withDefaults());
    }
}
