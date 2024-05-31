package edu.examples.todos.features.clients.sign_in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractSignInEndpoint implements SignInEndpoint
{
    private final SignIn signIn;
    private final SignInResponseAssembler responseAssembler;

    @Override
    public SignInResponse run(@Valid SignInRequest request)
    {
        var reply = signIn.run(request);

        return responseAssembler.toModel(reply);
    }
}
