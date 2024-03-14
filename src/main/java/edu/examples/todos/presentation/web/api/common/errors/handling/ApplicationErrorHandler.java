package edu.examples.todos.presentation.web.api.common.errors.handling;

import edu.examples.todos.presentation.web.api.common.errors.ApplicationError;
import edu.examples.todos.usecases.common.exceptions.UseCasesException;
import edu.examples.todos.usecases.todos.accounting.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler({
            UseCasesException.class
    })
    public ResponseEntity<ApplicationError> handleApplicationException(UseCasesException exception)
    {
        return ResponseEntity.badRequest().body(new ApplicationError(exception.getMessage()));
    }
}
