package edu.examples.todos.domain.resources.users;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class UserException extends DomainException
{
    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}
