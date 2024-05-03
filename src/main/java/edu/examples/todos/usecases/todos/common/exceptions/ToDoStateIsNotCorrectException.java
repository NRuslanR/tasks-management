package edu.examples.todos.usecases.todos.common.exceptions;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ToDoStateIsNotCorrectException extends UseCasesException
{
    public ToDoStateIsNotCorrectException()
    {
        super("To-Do's state isn't correct");
    }

    public ToDoStateIsNotCorrectException(String message)
    {
        super(message);
    }
}
