package edu.examples.todos.features.clients.get_client_info;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

public interface GetClientInfoEndpoint
{
    Mono<ClientInfoResource> handle(@NotBlank String clientId) throws NullPointerException;
}
