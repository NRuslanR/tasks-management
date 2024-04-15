package edu.examples.todos.domain.actors.todos;

public class ToDoStatusIsNotCorrectDomainException extends ToDoException
{
    public ToDoStatusIsNotCorrectDomainException() {
    }

    public ToDoStatusIsNotCorrectDomainException(String message) {
        super(message);
    }
}
