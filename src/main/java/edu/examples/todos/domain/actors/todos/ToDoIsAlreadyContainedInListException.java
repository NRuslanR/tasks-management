package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class ToDoIsAlreadyContainedInListException extends DomainException
{
    public ToDoIsAlreadyContainedInListException() {
        super("To-Do is already contained in list");
    }

    public ToDoIsAlreadyContainedInListException(String message) {
        super(message);
    }

    public ToDoIsAlreadyContainedInListException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToDoIsAlreadyContainedInListException(Throwable cause) {
        super(cause);
    }

    public ToDoIsAlreadyContainedInListException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
