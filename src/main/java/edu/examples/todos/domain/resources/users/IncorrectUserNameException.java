package edu.examples.todos.domain.resources.users;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class IncorrectUserNameException extends UserException
{
    public IncorrectUserNameException() {
    }

    public IncorrectUserNameException(String message) {
        super(message);
    }
}
