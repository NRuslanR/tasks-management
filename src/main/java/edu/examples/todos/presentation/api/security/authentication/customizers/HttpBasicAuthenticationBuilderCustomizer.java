package edu.examples.todos.presentation.api.security.authentication.customizers;

import edu.examples.todos.presentation.api.security.config.HttpBasicRealmValue;
import lombok.SneakyThrows;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class HttpBasicAuthenticationBuilderCustomizer implements AuthenticationBuilderCustomizer
{
    @HttpBasicRealmValue
    private String realmName;

    @SneakyThrows
    @Override
    public HttpSecurity customizeAuthenticationBuilder(HttpSecurity httpSecurity)
    {
        return
                httpSecurity
                    .httpBasic(c -> c.realmName(realmName));
    }
}
