package edu.examples.todos.domain.actors.todos;

public class ToDoNameInCorrectException extends ToDoException
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
