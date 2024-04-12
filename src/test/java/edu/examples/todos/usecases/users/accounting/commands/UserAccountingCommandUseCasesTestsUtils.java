package edu.examples.todos.usecases.users.accounting.commands;

import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.remove.RemoveUserCommand;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

    public static Collection<RemoveUserCommand> createIncorrectCommandsForUserRemoving()
    {
        return List.of(
                new RemoveUserCommand(),
                new RemoveUserCommand("  ")
        );
    }

    public static RemoveUserCommand createCommandForNonExistentUserRemoving()
    {
        return new RemoveUserCommand(UUID.randomUUID().toString());
    }
}
