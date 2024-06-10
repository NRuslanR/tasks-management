package edu.examples.todos.usecases.todos.accounting.commands.update;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectUpdateToDoCommandException extends UseCasesException
{
    public IncorrectUpdateToDoCommandException()
    {
        super("Failed to update To-Do due to incorrect input");
    }

    public IncorrectUpdateToDoCommandException(String message) {
        super(message);
    }

    public IncorrectUpdateToDoCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectUpdateToDoCommandException(Throwable cause) {
        super(cause);
    }

    public IncorrectUpdateToDoCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
