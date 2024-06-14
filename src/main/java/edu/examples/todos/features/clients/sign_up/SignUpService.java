package edu.examples.todos.features.clients.sign_up;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface SignUpService
{
    Mono<SignUpReply> signUp(@Valid SignUpRequest request) throws NullPointerException, ClientAlreadyExistsException;
}
