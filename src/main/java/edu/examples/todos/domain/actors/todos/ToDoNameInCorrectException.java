package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class ToDoNameInCorrectException extends DomainException
{
    public ToDoNameInCorrectException()
    {
        super("ToDo's name is incorrect");
    }

    public ToDoNameInCorrectException(String message) {
        super(message);
    }

    public ToDoNameInCorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToDoNameInCorrectException(Throwable cause) {
        super(cause);
    }

    public ToDoNameInCorrectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
