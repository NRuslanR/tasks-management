package edu.examples.todos.presentation.api.features.users.accounting;

import edu.examples.todos.presentation.api.features.users.common.resources.UserResource;
import edu.examples.todos.presentation.api.features.users.common.resources.UserResourceAssembler;
import edu.examples.todos.presentation.api.security.authorization.AdminsAllowed;
import edu.examples.todos.usecases.users.accounting.UserDto;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCases;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserResult;
import edu.examples.todos.usecases.users.accounting.commands.remove.RemoveUserCommand;
import edu.examples.todos.usecases.users.accounting.common.exceptions.UserNotFoundException;
import edu.examples.todos.usecases.users.accounting.queries.UserAccountingQueryUseCases;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdQuery;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdResult;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/*
refactor: getUserById can be requested by authenticated principal who has same related id
 */
@RequiredArgsConstructor
@AdminsAllowed
public class AbstractApiUserAccountingController implements ApiUserAccountingController
{
    private final UserAccountingQueryUseCases userAccountingQueryUseCases;

    private final UserAccountingCommandUseCases userAccountingCommandUseCases;

    private final UserResourceAssembler userResourceAssembler;

    @Override
    public Mono<UserResource> getUserById(String userId)
    {
        return
                userAccountingQueryUseCases
                    .getUserById(new GetUserByIdQuery(userId))
                    .map(GetUserByIdResult::getUser)
                    .flatMap(this::toUserResource);
    }

    @Override
    public Mono<UserResource> createUser(CreateUserCommand command)
    {
        return
                userAccountingCommandUseCases
                    .createUser(command)
                    .map(CreateUserResult::getUser)
                    .flatMap(this::toUserResource);
    }

    @Override
    public Mono<Void> removeUser(String userId)
    {
        return
                userAccountingCommandUseCases
                        .removeUser(new RemoveUserCommand(userId))
                        .onErrorComplete(UserNotFoundException.class)
                        .then();
    }

    private Mono<UserResource> toUserResource(UserDto userDto)
    {
        return Mono.fromCallable(() -> userResourceAssembler.toModel(userDto)) ;
    }
}
