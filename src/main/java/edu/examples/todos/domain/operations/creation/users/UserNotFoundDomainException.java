package edu.examples.todos.domain.operations.creation.users;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class UserNotFoundDomainException extends DomainException
{
    public UserNotFoundDomainException() {
        super("User not found");
    }

    public UserNotFoundDomainException(String message) {
        super(message);
    }
}
