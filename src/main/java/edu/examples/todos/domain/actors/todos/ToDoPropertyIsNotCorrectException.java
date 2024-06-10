package edu.examples.todos.domain.actors.todos;

public class ToDoPropertyIsNotCorrectException extends ToDoException
{
    public ToDoPropertyIsNotCorrectException() {
        super("To-Do's property isn't correct");
    }

    public ToDoPropertyIsNotCorrectException(String message) {
        super(message);
    }
}
