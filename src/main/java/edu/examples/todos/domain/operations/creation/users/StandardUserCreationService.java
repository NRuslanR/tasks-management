package edu.examples.todos.domain.operations.creation.users;

import edu.examples.todos.domain.resources.users.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StandardUserCreationService implements UserCreationService
{
    @Override
    public Mono<CreateUserReply> createUserAsync(CreateUserRequest request)
    {
        return
                ensureCreateUserRequestIsValid(request)
                    .flatMap(this::doCreateUser);
    }

    private Mono<CreateUserReply> doCreateUser(CreateUserRequest request)
    {
        return
                Mono
                    .fromCallable(() -> createUserFrom(request))
                    .onErrorResume(
                            UserException.class,
                            e -> Mono.error(new IncorrectCreateUserRequestException(e.getMessage()))
                    )
                    .map(CreateUserReply::new);
    }

    private Mono<CreateUserRequest> ensureCreateUserRequestIsValid(CreateUserRequest request)
    {
        return
                Mono.fromCallable(
                    () -> {
                        var req = Objects.requireNonNull(request);

                        Objects.requireNonNull(req.getName());

                        return req;
                    }
                );
    }

    private User createUserFrom(CreateUserRequest request)
    {
        return User.of(
                UserId.of(UUID.randomUUID()),
                request.getName(),
                createUserClaimsFrom(request)
        );
    }

    private UserClaims createUserClaimsFrom(CreateUserRequest request)
    {
        return UserClaims.of(
            Optional.ofNullable(request.getAllowedToDoCreationCount()).orElse(Integer.MAX_VALUE),
            Optional.ofNullable(request.getEditForeignTodosAllowed()).orElse(false),
            Optional.ofNullable(request.getRemoveForeignTodosAllowed()).orElse(false),
            Optional.ofNullable(request.getPerformForeignTodosAllowed()).orElse(false)
        );
    }
}
