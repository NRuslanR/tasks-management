package edu.examples.todos.features.clients.sign_in;

import edu.examples.todos.features.clients.shared.ClientInfoResourceAssembler;
import edu.examples.todos.presentation.api.common.resources.assemblers.LinkedRepresentationModelAssemblerSupport;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SignInResponseAssembler extends LinkedRepresentationModelAssemblerSupport<SignInReply, SignInResponse>
{
    @Autowired
    private ClientInfoResourceAssembler clientInfoResourceAssembler;

    public SignInResponseAssembler(Class<?> controllerClass, Class<SignInResponse> resourceType)
    {
        super(controllerClass, resourceType);
    }

    @Override
    protected SignInResponse instantiateModel(SignInReply entity)
    {
        return SignInResponse.of(clientInfoResourceAssembler.toModel(entity.getClientInfo()));
    }
}
