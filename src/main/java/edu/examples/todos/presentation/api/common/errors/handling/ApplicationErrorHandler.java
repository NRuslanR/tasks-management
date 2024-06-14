package edu.examples.todos.presentation.api.common.errors.handling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import edu.examples.todos.domain.actors.todos.ToDoActionIsNotAvailableException;
import edu.examples.todos.features.shared.FeatureException;
import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import edu.examples.todos.presentation.api.common.exceptions.EntityNotFoundUseCaseException;
import edu.examples.todos.usecases.common.exceptions.UseCasesException;
import jakarta.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Order
@Slf4j
public class ApplicationErrorHandler extends ResponseEntityExceptionHandler
{
    @Autowired
    private ServerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private ServerAuthenticationEntryPoint authenticationEntryPoint;
    @ExceptionHandler({
            UseCasesException.class,
            FeatureException.class
    })
    public Mono<ResponseEntity<ApplicationError>> handleUseCasesException(Exception exception)
    {
        return Mono.just(
                ResponseEntity
                        .badRequest()
                        .body(new ApplicationError(exception.getMessage())
                )
        );
    }

    @SneakyThrows
    @ExceptionHandler(AuthenticationException.class)
    public Mono<Void> handleAuthenticationException(
        AuthenticationException exception,
        ServerWebExchange serverWebExchange
        
    )
    {
        return authenticationEntryPoint.commence(serverWebExchange, exception);
    }

    @SneakyThrows
    @ExceptionHandler({
            AccessDeniedException.class,
            ToDoActionIsNotAvailableException.class
    })
    public Mono<Void> handleAccessDeniedException(
        Exception exception,
        ServerWebExchange serverWebExchange
    )
    {
        var accessDeniedException =
                exception instanceof AccessDeniedException ?
                        (AccessDeniedException)exception :
                        new AccessDeniedException(exception.getMessage());

        return accessDeniedHandler.handle(serverWebExchange, accessDeniedException);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<ApplicationError> handleConstraintViolationException(ConstraintViolationException exception)
    {
        return Mono.just(new ApplicationError(exception.getMessage()));
    }

    @ExceptionHandler({
            EntityNotFoundUseCaseException.class
    })
    public ResponseEntity<ApplicationError> handleEntityNotFoundUseCaseException(EntityNotFoundUseCaseException exception)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApplicationError(exception.getMessage()));
    }

    public Mono<ResponseEntity<Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    )
    {
        return Mono.just(
                ResponseEntity.badRequest().body(
                        new ApplicationError(
                                StringUtils.collectionToDelimitedString(
                                exception
                                        .getBindingResult()
                                        .getFieldErrors()
                                        .stream()
                                        .map(FieldError::getDefaultMessage)
                                        .toList(),
                        "."
                                )
                        )
                )
        );
    }

    @ExceptionHandler(Exception.class)
    protected Mono<ResponseEntity<Object>> handleApplicationException(Exception exception)
    {
        log.error("", exception);

        return Mono.just(
                ResponseEntity.internalServerError().body(
                        new ApplicationError("The internal error occurred during the handling this request")
                )
        );
    }
}
