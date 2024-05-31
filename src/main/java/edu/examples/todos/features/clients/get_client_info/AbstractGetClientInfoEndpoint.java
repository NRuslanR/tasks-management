package edu.examples.todos.features.clients.get_client_info;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import edu.examples.todos.features.clients.shared.ClientInfoResourceAssembler;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
public abstract class AbstractGetClientInfoEndpoint implements GetClientInfoEndpoint
{
    private final GetClientInfo getClientInfo;
    private final ClientInfoResourceAssembler resourceAssembler;

    @Override
    @PreAuthorize("isAuthenticated() && ((#clientId == authentication.principal.username && hasRole('USER')) || hasRole('ADMIN'))")
    public ClientInfoResource handle(@NotBlank String clientId) throws NullPointerException
    {
        var result = getClientInfo.run(GetClientInfoQuery.of(clientId));

        return resourceAssembler.toModel(result.getClientInfo());
    }
}
