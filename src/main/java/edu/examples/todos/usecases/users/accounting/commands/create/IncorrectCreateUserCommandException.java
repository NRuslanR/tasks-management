package edu.examples.todos.usecases.users.accounting.commands.create;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectCreateUserCommandException extends UseCasesException
{
    public IncorrectCreateUserCommandException() {
    }

    public IncorrectCreateUserCommandException(String message) {
        super(message);
    }
}
