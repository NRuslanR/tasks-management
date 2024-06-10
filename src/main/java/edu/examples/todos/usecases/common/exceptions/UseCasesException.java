package edu.examples.todos.usecases.common.exceptions;

public class UseCasesException extends RuntimeException
{
    public UseCasesException() {
    }

    public UseCasesException(String message) {
        super(message);
    }

    public UseCasesException(String message, Throwable cause) {
        super(message, cause);
    }

    public UseCasesException(Throwable cause) {
        super(cause);
    }

    public UseCasesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
