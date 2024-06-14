package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

public interface ClientDetailsService
{
    Mono<ClientDetails> getClientDetails(@NotBlank String clientId) throws NullPointerException, ClientDetailsNotFoundException;

    Mono<ClientDetails> createClientDetails(@Valid ClientDetails clientDetails) throws NullPointerException;

    Mono<ClientDetails> updateClientDetails(@Valid ClientDetails clientDetails) throws NullPointerException, ClientDetailsNotFoundException;

    Mono<Void> removeClientDetails(@NotBlank String clientId) throws NullPointerException;
}
