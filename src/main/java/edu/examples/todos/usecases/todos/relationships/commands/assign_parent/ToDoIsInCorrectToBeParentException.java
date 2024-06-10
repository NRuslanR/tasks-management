package edu.examples.todos.usecases.todos.relationships.commands.assign_parent;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ToDoIsInCorrectToBeParentException extends UseCasesException
{
    public ToDoIsInCorrectToBeParentException() {
    }

    public ToDoIsInCorrectToBeParentException(String message) {
        super(message);
    }

    public ToDoIsInCorrectToBeParentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToDoIsInCorrectToBeParentException(Throwable cause) {
        super(cause);
    }

    public ToDoIsInCorrectToBeParentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
