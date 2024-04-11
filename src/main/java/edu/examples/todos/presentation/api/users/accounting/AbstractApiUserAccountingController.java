package edu.examples.todos.presentation.api.users.accounting;

import edu.examples.todos.presentation.api.users.common.resources.UserResource;
import edu.examples.todos.presentation.api.users.common.resources.UserResourceAssembler;
import edu.examples.todos.usecases.users.accounting.UserDto;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCases;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserResult;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbstractApiUserAccountingController implements ApiUserAccountingController
{
    private final UserAccountingCommandUseCases userAccountingCommandUseCases;

    private final UserResourceAssembler userResourceAssembler;

    @Override
    public Mono<UserResource> createUser(CreateUserCommand command)
    {
        return
                userAccountingCommandUseCases
                        .createUser(command)
                        .map(CreateUserResult::getUser)
                        .flatMap(this::toUserResource);
    }

    private Mono<UserResource> toUserResource(UserDto userDto)
    {
        return Mono.fromCallable(() -> userResourceAssembler.toModel(userDto)) ;
    }
}
