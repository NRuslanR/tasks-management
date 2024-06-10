package edu.examples.todos.usecases.todos.accounting.commands.create;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ToDoAlreadyExistsException extends UseCasesException
{
    public ToDoAlreadyExistsException() {
    }

    public ToDoAlreadyExistsException(String message) {
        super(message);
    }

    public ToDoAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToDoAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ToDoAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
