package edu.examples.todos.features.clients.sign_in;

import edu.examples.todos.features.clients.get_client_info.ClientInfoNotFoundException;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface SignIn
{
    Mono<SignInReply> run(@Valid SignInRequest request) throws NullPointerException, ClientInfoNotFoundException;
}
