package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class ToDoAlreadyExistsDomainException extends DomainException
{
    public ToDoAlreadyExistsDomainException(String toDoName)
    {
        super("To-Do \"" + toDoName + "\" is already exists");
    }

    public ToDoAlreadyExistsDomainException()
    {
    }

    public ToDoAlreadyExistsDomainException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ToDoAlreadyExistsDomainException(Throwable cause)
    {
        super(cause);
    }

    public ToDoAlreadyExistsDomainException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
