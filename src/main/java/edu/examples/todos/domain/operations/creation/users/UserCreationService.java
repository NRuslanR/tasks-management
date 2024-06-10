package edu.examples.todos.domain.operations.creation.users;

import reactor.core.publisher.Mono;

public interface UserCreationService
{
    Mono<CreateUserReply> createUserAsync(CreateUserRequest request);
}
