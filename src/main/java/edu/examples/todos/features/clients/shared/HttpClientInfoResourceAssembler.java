package edu.examples.todos.features.clients.shared;

import edu.examples.todos.features.clients.get_client_info.HttpGetClientInfoEndpoint;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HttpClientInfoResourceAssembler extends ClientInfoResourceAssembler
{
    public HttpClientInfoResourceAssembler()
    {
        super(Object.class, ClientInfoResource.class);
    }

    @Override
    protected Link getClientInfoLink(ClientInfo entity)
    {
        return linkTo(methodOn(HttpGetClientInfoEndpoint.class).handle(entity.getClientId())).withSelfRel();
    }
}
