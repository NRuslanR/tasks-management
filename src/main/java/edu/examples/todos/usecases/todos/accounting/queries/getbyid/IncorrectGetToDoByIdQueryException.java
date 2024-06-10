package edu.examples.todos.usecases.todos.accounting.queries.getbyid;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectGetToDoByIdQueryException extends UseCasesException
{
    public IncorrectGetToDoByIdQueryException()
    {
        super("Incorrect query to get To-Do");
    }

    public IncorrectGetToDoByIdQueryException(String message)
    {
        super(message);
    }

    public IncorrectGetToDoByIdQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public IncorrectGetToDoByIdQueryException(Throwable cause)
    {
        super(cause);
    }

    public IncorrectGetToDoByIdQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
