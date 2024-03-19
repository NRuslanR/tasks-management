package edu.examples.todos.usecases.todos.accounting.queries.findbyid;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectGetByIdQueryException extends UseCasesException
{
    public IncorrectGetByIdQueryException()
    {
        super("Incorrect query to get To-Do");
    }

    public IncorrectGetByIdQueryException(String message)
    {
        super(message);
    }

    public IncorrectGetByIdQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public IncorrectGetByIdQueryException(Throwable cause)
    {
        super(cause);
    }

    public IncorrectGetByIdQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
