package edu.examples.todos.usecases.todos.accounting;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ToDoNotFoundException extends UseCasesException
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
