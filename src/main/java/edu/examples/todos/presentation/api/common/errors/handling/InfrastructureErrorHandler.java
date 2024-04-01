package edu.examples.todos.presentation.api.common.errors.handling;

import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InfrastructureErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler({
            OptimisticLockingFailureException.class
    })
    public ResponseEntity<ApplicationError> handleException(OptimisticLockingFailureException exception)
    {
        return
                ResponseEntity
                        .internalServerError()
                        .body(
                                new ApplicationError(
                                        "The attempt to simultaneously deal with same objects against other clients"
                                )
                        );
    }
}
