package edu.examples.todos.presentation.api.common.errors.handling;

import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import edu.examples.todos.presentation.api.common.exceptions.ApiException;
import edu.examples.todos.usecases.common.exceptions.UseCasesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
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
            ApiException.class
    })
    public ResponseEntity<ApplicationError> handleApiException(UseCasesException exception)
    {
        return ResponseEntity.badRequest().body(new ApplicationError(exception.getMessage()));
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
