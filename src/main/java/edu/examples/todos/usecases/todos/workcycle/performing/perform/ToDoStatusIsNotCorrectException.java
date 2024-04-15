package edu.examples.todos.usecases.todos.workcycle.performing.perform;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ToDoStatusIsNotCorrectException extends UseCasesException
{
    public ToDoStatusIsNotCorrectException() {
        super("To-Do isn't at allowed work cycle stage");
    }

    public ToDoStatusIsNotCorrectException(String message) {
        super(message);
    }
}
