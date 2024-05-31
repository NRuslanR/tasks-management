package edu.examples.todos.features.clients.shared;

import edu.examples.todos.presentation.api.common.resources.assemblers.LinkedRepresentationModelAssemblerSupport;
import org.springframework.hateoas.Link;

import java.util.List;

public abstract class ClientInfoResourceAssembler extends LinkedRepresentationModelAssemblerSupport<ClientInfo, ClientInfoResource>
{
    public ClientInfoResourceAssembler(Class<?> controllerClass, Class<ClientInfoResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected ClientInfoResource instantiateModel(ClientInfo entity)
    {
        return new ClientInfoResource(entity);
    }

    @Override
    protected void setLinksToModel(ClientInfoResource resource, ClientInfo entity)
    {
        resource.add(createLinks(entity));
    }

    private Iterable<Link> createLinks(ClientInfo entity)
    {
        return List.of(
                getClientInfoLink(entity)
        );
    }

    protected abstract Link getClientInfoLink(ClientInfo entity);
}
