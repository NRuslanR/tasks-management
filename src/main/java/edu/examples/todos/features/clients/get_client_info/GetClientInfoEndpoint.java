package edu.examples.todos.features.clients.get_client_info;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import jakarta.validation.constraints.NotBlank;

public interface GetClientInfoEndpoint
{
    ClientInfoResource handle(@NotBlank String clientId) throws NullPointerException;
}
