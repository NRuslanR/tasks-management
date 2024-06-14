package edu.examples.todos.features.clients.sign_up;

import reactor.core.publisher.Mono;

public interface SignUpEndpoint
{
    Mono<SignUpResponse> signUp(SignUpRequest request);
}
