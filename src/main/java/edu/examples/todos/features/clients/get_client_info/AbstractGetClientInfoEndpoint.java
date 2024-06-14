package edu.examples.todos.features.clients.get_client_info;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import edu.examples.todos.features.clients.shared.ClientInfoResourceAssembler;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Validated
public abstract class AbstractGetClientInfoEndpoint implements GetClientInfoEndpoint
{
    private final GetClientInfo getClientInfo;
    private final ClientInfoResourceAssembler resourceAssembler;

    @Override
    @PreAuthorize("isAuthenticated() && ((#clientId == authentication.principal.username && hasRole('USER')) || hasRole('ADMIN'))")
    public Mono<ClientInfoResource> handle(@NotBlank String clientId) throws NullPointerException
    {
        return 
            getClientInfo
                .run(GetClientInfoQuery.of(clientId))
                .map(result -> resourceAssembler.toModel(result.getClientInfo()));
    }
}
