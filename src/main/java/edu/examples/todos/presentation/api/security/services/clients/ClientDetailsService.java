package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface ClientDetailsService
{
    ClientDetails getClientDetails(@NotBlank String clientId) throws NullPointerException, ClientDetailsNotFoundException;

    ClientDetails createClientDetails(@Valid ClientDetails clientDetails) throws NullPointerException;

    ClientDetails updateClientDetails(@Valid ClientDetails clientDetails) throws NullPointerException, ClientDetailsNotFoundException;

    void removeClientDetails(@NotBlank String clientId) throws NullPointerException;
}
