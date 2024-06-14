package edu.examples.todos.features.clients.get_client_info;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import edu.examples.todos.features.clients.shared.ClientInfoResourceAssembler;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clients")
public class HttpGetClientInfoEndpoint extends AbstractGetClientInfoEndpoint
{
    public HttpGetClientInfoEndpoint(GetClientInfo getClientInfo, ClientInfoResourceAssembler resourceAssembler)
    {
        super(getClientInfo, resourceAssembler);
    }

    @Override
    @GetMapping(path = "/{clientId}")
    public Mono<ClientInfoResource> handle(@PathVariable("clientId") String clientId) throws NullPointerException
    {
        return super.handle(clientId);
    }
}
