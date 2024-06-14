package edu.examples.todos.presentation.api.security.authentication.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApiAccessDeniedHandler implements ServerAccessDeniedHandler
{
    private final ObjectMapper jacksonMapper;

    @SneakyThrows
    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            AccessDeniedException accessDeniedException
    ) 
    {
        var response = exchange.getResponse();

        response.setStatusCode(HttpStatus.FORBIDDEN);

        var headers = response.getHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        var responseBody = 
            jacksonMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(new ApplicationError(accessDeniedException.getMessage()));

        var responseBodyBuffer = response.bufferFactory().wrap(responseBody.getBytes("UTF-8"));

        return response.writeWith(Mono.just(responseBodyBuffer));
    }
}
