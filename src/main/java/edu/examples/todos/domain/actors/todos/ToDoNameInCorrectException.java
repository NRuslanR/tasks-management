package edu.examples.todos.domain.actors.todos;

public class ToDoNameInCorrectException extends ToDoPropertyIsNotCorrectException
{
    public ToDoNameInCorrectException()
    {
        super("ToDo's name is incorrect");
    }

    public ToDoNameInCorrectException(String message) {
        super(message);
    }
}
