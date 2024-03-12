package edu.examples.todos.presentation.web.api.common.errors.handling;

import edu.examples.todos.domain.actors.todos.ToDoNameInCorrectException;
import edu.examples.todos.presentation.web.api.common.errors.ApplicationError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(ToDoNameInCorrectException.class)
    public ResponseEntity<ApplicationError> handleToDoCreationalInfoInvalidException(ToDoNameInCorrectException exception)
    {
        return ResponseEntity.badRequest().body(new ApplicationError(exception.getMessage()));
    }
}
