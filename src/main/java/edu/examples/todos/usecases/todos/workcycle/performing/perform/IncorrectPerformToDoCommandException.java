package edu.examples.todos.usecases.todos.workcycle.performing.perform;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectPerformToDoCommandException extends UseCasesException
{
    public IncorrectPerformToDoCommandException() {
        super("Incorrect command to perform To-Do");
    }

    public IncorrectPerformToDoCommandException(String message) {
        super(message);
    }
}
