package edu.examples.todos.domain.actors.todos;

public class ToDoActionIsNotAvailableException extends ToDoException
{
    public ToDoActionIsNotAvailableException() {
        super("To-Do's action isn't available");
    }

    public ToDoActionIsNotAvailableException(String message) {
        super(message);
    }
}
