package edu.examples.todos.features.clients.get_client_info;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface GetClientInfo
{
    Mono<GetClientInfoResult> run(@Valid GetClientInfoQuery query) throws NullPointerException, ClientInfoNotFoundException;
}
