package edu.examples.todos.domain.actors.todos;

public class IncorrectToDoPriorityValueException extends ToDoPropertyIsNotCorrectException
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
