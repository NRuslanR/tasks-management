package edu.examples.todos.usecases.users.accounting.commands;

import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserResult;
import edu.examples.todos.usecases.users.accounting.commands.create.IncorrectCreateUserCommandException;
import reactor.core.publisher.Mono;

public interface UserAccountingCommandUseCases
{
    /**
     * @param command
     * @return CreateUserResult
     * @throws NullPointerException when command is null
     * @throws IncorrectCreateUserCommandException when command is incorrect
     * */
    Mono<CreateUserResult> createUser(CreateUserCommand command);
}
