package edu.examples.todos.domain.actors.todos;

public class IncorrectToDoPriorityTypeException extends ToDoException
{
    public IncorrectToDoPriorityTypeException() {
        super("Incorrect To-Do priority type");
    }

    public IncorrectToDoPriorityTypeException(String message) {
        super(message);
    }
}
