package edu.examples.todos.domain.resources.users;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class IncorrectToDoCreationCountException extends UserException
{
    public IncorrectToDoCreationCountException()
    {
    }

    public IncorrectToDoCreationCountException(String message)
    {
        super(message);
    }
}
