package edu.examples.todos.domain.operations.accounting.todos;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class ToDoNotFoundDomainException extends DomainException
{
    public ToDoNotFoundDomainException()
    {
        super("To-Do not found");
    }

    public ToDoNotFoundDomainException(String message) {
        super(message);
    }
}
