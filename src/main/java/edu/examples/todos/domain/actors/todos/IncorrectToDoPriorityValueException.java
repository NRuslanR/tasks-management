package edu.examples.todos.domain.actors.todos;

public class IncorrectToDoPriorityValueException extends ToDoException
{
    public IncorrectToDoPriorityValueException()
    {
        super("Incorrect To-Do priority value exception");
    }

    public IncorrectToDoPriorityValueException(String message)
    {
        super(message);
    }
}
