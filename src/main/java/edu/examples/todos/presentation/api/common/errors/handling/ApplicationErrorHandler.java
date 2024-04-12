package edu.examples.todos.presentation.api.common.errors.handling;

import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import edu.examples.todos.presentation.api.common.exceptions.EntityNotFoundUseCaseException;
import edu.examples.todos.usecases.common.exceptions.UseCasesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order
@Slf4j
public class ApplicationErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler({
            UseCasesException.class
    })
    public ResponseEntity<ApplicationError> handleUseCasesException(UseCasesException exception)
    {
        return ResponseEntity.badRequest().body(new ApplicationError(exception.getMessage()));
    }

    @ExceptionHandler({
            EntityNotFoundUseCaseException.class
    })
    public ResponseEntity<ApplicationError> handleEntityNotFoundUseCaseException(EntityNotFoundUseCaseException exception)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApplicationError(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleApplicationException(Exception exception)
    {
        log.error("", exception);

        return ResponseEntity.internalServerError().body(
                new ApplicationError("The internal error occurred during the handling this request")
        );
    }
}
