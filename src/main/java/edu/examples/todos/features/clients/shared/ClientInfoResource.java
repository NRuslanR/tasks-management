package edu.examples.todos.features.clients.shared;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

public class ClientInfoResource extends EntityModel<ClientInfo>
{
    public static ClientInfoResource of(ClientInfo clientInfo, Link... links)
    {
        return new ClientInfoResource(clientInfo, links);
    }

    public ClientInfoResource(ClientInfo clientInfo, Link... links)
    {
        super(clientInfo, List.of(links));
    }
}
