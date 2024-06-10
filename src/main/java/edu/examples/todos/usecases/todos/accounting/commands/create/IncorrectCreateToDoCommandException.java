package edu.examples.todos.usecases.todos.accounting.commands.create;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectCreateToDoCommandException extends UseCasesException
{
    public IncorrectCreateToDoCommandException() {
    }

    public IncorrectCreateToDoCommandException(String message) {
        super(message);
    }

    public IncorrectCreateToDoCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectCreateToDoCommandException(Throwable cause) {
        super(cause);
    }

    public IncorrectCreateToDoCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
