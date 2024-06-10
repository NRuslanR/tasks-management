package edu.examples.todos.domain.actors.todos;

public class ToDoStateIsNotCorrectDomainException extends ToDoException
{
    public ToDoStateIsNotCorrectDomainException()
    {
        super("To-Do's state isn't correct");
    }

    public ToDoStateIsNotCorrectDomainException(String message) {
        super(message);
    }
}
