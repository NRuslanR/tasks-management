package edu.examples.todos.features.clients.sign_up;

import edu.examples.todos.features.clients.shared.ClientInfoResourceAssembler;
import edu.examples.todos.presentation.api.common.resources.assemblers.LinkedRepresentationModelAssemblerSupport;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SignUpResponseAssembler extends LinkedRepresentationModelAssemblerSupport<SignUpReply, SignUpResponse>
{
    @Autowired
    private ClientInfoResourceAssembler clientInfoResourceAssembler;

    public SignUpResponseAssembler(Class<?> controllerClass, Class<SignUpResponse> resourceType)
    {
        super(controllerClass, resourceType);
    }

    @Override
    protected SignUpResponse instantiateModel(SignUpReply entity)
    {
        var clientInfoResource = clientInfoResourceAssembler.toModel(entity.getClientInfo());

        return SignUpResponse.of(clientInfoResource);
    }
}
