package edu.examples.todos.presentation.api.security.authentication.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements ServerAuthenticationEntryPoint
{
    private final ObjectMapper jacksonMapper;

    private final String realmName;

    @SneakyThrows
    @Override
    public Mono<Void> commence(
            ServerWebExchange exchange,
            AuthenticationException authException
    ) 
    {
        var response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        var headers = response.getHeaders();

        headers.set("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
        
        headers.setContentType(MediaType.APPLICATION_JSON);

        var responseBody = 
            jacksonMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(new ApplicationError(authException.getMessage()));

        
        var responseBodyBuffer = response.bufferFactory().wrap(responseBody.getBytes("UTF-8"));

        return response.writeWith(Mono.just(responseBodyBuffer));
    }
}
