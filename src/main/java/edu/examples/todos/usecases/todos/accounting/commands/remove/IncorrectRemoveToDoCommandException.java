package edu.examples.todos.usecases.todos.accounting.commands.remove;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectRemoveToDoCommandException extends UseCasesException
{
    public IncorrectRemoveToDoCommandException() {
        super();
    }

    public IncorrectRemoveToDoCommandException(String message) {
        super(message);
    }

    public IncorrectRemoveToDoCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectRemoveToDoCommandException(Throwable cause) {
        super(cause);
    }

    public IncorrectRemoveToDoCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
