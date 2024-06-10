package edu.examples.todos.usecases.todos.accounting.commands.update;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class SavingToDoException extends UseCasesException
{
    public SavingToDoException() {
    }

    public SavingToDoException(String message) {
        super(message);
    }

    public SavingToDoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingToDoException(Throwable cause) {
        super(cause);
    }

    public SavingToDoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
