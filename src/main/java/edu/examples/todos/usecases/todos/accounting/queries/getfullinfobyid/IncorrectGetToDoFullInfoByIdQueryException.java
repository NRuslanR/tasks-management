package edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectGetToDoFullInfoByIdQueryException extends UseCasesException
{
    public IncorrectGetToDoFullInfoByIdQueryException()
    {
        super("Incorrect query to get To-Do's full info");
    }

    public IncorrectGetToDoFullInfoByIdQueryException(String message) {
        super(message);
    }

    public IncorrectGetToDoFullInfoByIdQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectGetToDoFullInfoByIdQueryException(Throwable cause) {
        super(cause);
    }

    public IncorrectGetToDoFullInfoByIdQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
