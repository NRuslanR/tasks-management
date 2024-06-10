package edu.examples.todos.domain.operations.creation.users;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class IncorrectCreateUserRequestException extends DomainException
{
    public IncorrectCreateUserRequestException() {
    }

    public IncorrectCreateUserRequestException(String message) {
        super(message);
    }
}
