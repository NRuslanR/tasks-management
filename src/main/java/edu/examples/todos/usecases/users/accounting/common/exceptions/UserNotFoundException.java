package edu.examples.todos.usecases.users.accounting.common.exceptions;

import edu.examples.todos.presentation.api.common.exceptions.EntityNotFoundUseCaseException;

public class UserNotFoundException extends EntityNotFoundUseCaseException
{
    public UserNotFoundException() {
        super("The required user not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
