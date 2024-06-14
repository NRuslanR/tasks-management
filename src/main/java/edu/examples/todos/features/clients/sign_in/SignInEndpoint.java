package edu.examples.todos.features.clients.sign_in;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface SignInEndpoint
{
    Mono<SignInResponse> run(@Valid SignInRequest request);
}
