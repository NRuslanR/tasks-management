package edu.examples.todos.presentation.api.common.errors.handling;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InfrastructureErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler({
            OptimisticLockingFailureException.class
    })
    public Mono<ResponseEntity<ApplicationError>> handleException(OptimisticLockingFailureException exception)
    {
        return Mono.just(
                ResponseEntity
                        .internalServerError()
                        .body(
                                new ApplicationError(
                                        "The attempt to simultaneously deal with same objects against other clients"
                                )
                        )
        );
    }
}
