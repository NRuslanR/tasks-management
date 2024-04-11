package edu.examples.todos.usecases.users.accounting.commands;

import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;

public class UserAccountingCommandUseCasesTestsUtils
{
    public static CreateUserCommand createSimpleCommandForRandomUserCreating()
    {
        return new CreateUserCommand("TestFirstName", "TestLastName");
    }

    public static CreateUserCommand createCommandForRandomUserCreatingWithRandomClaims()
    {
        return new CreateUserCommand(
                "TestFirstName",
                "TestLastName",
                16,
                false,
                true,
                false
        );
    }

    public static CreateUserCommand createIncorrectBlankCommandForUserCreating()
    {
            return new CreateUserCommand();
    }
}
