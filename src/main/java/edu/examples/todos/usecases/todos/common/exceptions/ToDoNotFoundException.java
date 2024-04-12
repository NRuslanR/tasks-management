package edu.examples.todos.usecases.todos.common.exceptions;

import edu.examples.todos.presentation.api.common.exceptions.EntityNotFoundUseCaseException;

public class ToDoNotFoundException extends EntityNotFoundUseCaseException
{
    public ToDoNotFoundException() {
        super("The required To-Do not found");
    }

    public ToDoNotFoundException(String message)
    {
        super(message);
    }

    public ToDoNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ToDoNotFoundException(Throwable cause)
    {
        super(cause);
    }

    public ToDoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
