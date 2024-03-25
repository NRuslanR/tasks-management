package edu.examples.todos.usecases.todos.accounting.queries.findtodos;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectFindToDosQueryException extends UseCasesException
{
    public IncorrectFindToDosQueryException()
    {
        super("Incorrect query to find To-Dos");
    }

    public IncorrectFindToDosQueryException(String message) {
        super(message);
    }

    public IncorrectFindToDosQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFindToDosQueryException(Throwable cause) {
        super(cause);
    }

    public IncorrectFindToDosQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
