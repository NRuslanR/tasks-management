package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class IncorrectCreateToDoRequestException extends DomainException
{
    public IncorrectCreateToDoRequestException() {
    }

    public IncorrectCreateToDoRequestException(String message) {
        super(message);
    }

    public IncorrectCreateToDoRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectCreateToDoRequestException(Throwable cause) {
        super(cause);
    }

    public IncorrectCreateToDoRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
