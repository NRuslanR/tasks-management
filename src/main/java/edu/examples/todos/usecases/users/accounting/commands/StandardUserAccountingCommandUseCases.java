package edu.examples.todos.usecases.users.accounting.commands;

import edu.examples.todos.domain.common.exceptions.DomainException;
import edu.examples.todos.domain.operations.creation.users.CreateUserReply;
import edu.examples.todos.domain.operations.creation.users.CreateUserRequest;
import edu.examples.todos.domain.operations.creation.users.IncorrectCreateUserRequestException;
import edu.examples.todos.domain.operations.creation.users.UserCreationService;
import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.persistence.repositories.users.UserRepository;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.users.accounting.UserDto;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserResult;
import edu.examples.todos.usecases.users.accounting.commands.create.IncorrectCreateUserCommandException;
import edu.examples.todos.usecases.users.accounting.commands.remove.IncorrectRemoveUserCommandException;
import edu.examples.todos.usecases.users.accounting.commands.remove.RemoveUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.remove.RemoveUserResult;
import edu.examples.todos.usecases.users.accounting.common.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StandardUserAccountingCommandUseCases implements UserAccountingCommandUseCases
{
    private final UserCreationService userCreationService;

    private final UseCaseMapper mapper;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Mono<CreateUserResult> createUser(CreateUserCommand command)
    {
        return
                ensureCreateUserCommandIsValid(command)
                        .flatMap(this::doCreateUser);
    }

    private Mono<CreateUserCommand> ensureCreateUserCommandIsValid(CreateUserCommand command)
    {
        return
                Mono.fromCallable(() -> Objects.requireNonNull(command));
    }

    private Mono<CreateUserResult> doCreateUser(CreateUserCommand createUserCommand)
    {
        return
                Mono
                    .fromCallable(() -> mapper.map(createUserCommand, CreateUserRequest.class))
                    .onErrorResume(
                            DomainException.class,
                            e -> Mono.error(new IncorrectCreateUserCommandException(e.getMessage()))
                    )
                    .flatMap(userCreationService::createUserAsync)
                    .onErrorResume(
                        IncorrectCreateUserRequestException.class,
                        e -> Mono.error(new IncorrectCreateUserCommandException(e.getMessage()))
                    )
                    .map(CreateUserReply::getUser)
                    .flatMap(this::saveUser)
                    .map(v -> new CreateUserResult(mapper.map(v, UserDto.class)));
    }

    @Override
    public Mono<RemoveUserResult> removeUser(RemoveUserCommand command)
    {
        return
                ensureRemoveUserCommandIsValid(command)
                        .flatMap(this::doRemoveUser);
    }

    private Mono<RemoveUserCommand> ensureRemoveUserCommandIsValid(RemoveUserCommand command)
    {
        return
                Mono.fromCallable(() -> {

                    Objects.requireNonNull(command);

                    if (StringUtils.hasText(command.getUserId()))
                        return command;

                    throw new IncorrectRemoveUserCommandException();
                });
    }

    private Mono<RemoveUserResult> doRemoveUser(RemoveUserCommand removeUserCommand)
    {
        return
                getUserById(removeUserCommand.getUserId())
                    .flatMap(this::deleteUser)
                    .map(v -> new RemoveUserResult(mapper.map(v, UserDto.class)));
    }

    private Mono<User> getUserById(String userId)
    {
        return
                Mono.fromCallable(
                    () ->
                        userRepository
                            .findById(UserId.of(userId))
                                .orElseThrow(UserNotFoundException::new)
                );
    }

    private Mono<User> saveUser(User user)
    {
        return Mono.fromCallable(() -> userRepository.save(user));
    }

    private Mono<User> deleteUser(User user)
    {
        return Mono.fromCallable(() -> {

            userRepository.delete(user);

            return user;

        });
    }
}
