package edu.examples.todos.presentation.api.common.exceptions;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class EntityNotFoundUseCaseException extends UseCasesException
{
    public EntityNotFoundUseCaseException() {
    }

    public EntityNotFoundUseCaseException(String message) {
        super(message);
    }

    public EntityNotFoundUseCaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundUseCaseException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundUseCaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
