package edu.examples.todos.presentation.api.users.accounting;

import edu.examples.todos.presentation.api.users.common.resources.UserResource;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import reactor.core.publisher.Mono;

public interface ApiUserAccountingController
{
    Mono<UserResource> getUserById(String userId);

    Mono<UserResource> createUser(CreateUserCommand command);

    Mono<Void> removeUser(String userId);
}
