package edu.examples.todos.usecases.users.accounting.commands.remove;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectRemoveUserCommandException extends UseCasesException
{
    public IncorrectRemoveUserCommandException()
    {
        super("Incorrect command to remove user");
    }

    public IncorrectRemoveUserCommandException(String message)
    {
        super(message);
    }
}
