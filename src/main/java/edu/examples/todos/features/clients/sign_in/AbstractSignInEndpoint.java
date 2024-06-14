package edu.examples.todos.features.clients.sign_in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class AbstractSignInEndpoint implements SignInEndpoint
{
    private final SignIn signIn;
    private final SignInResponseAssembler responseAssembler;

    @Override
    public Mono<SignInResponse> run(@Valid SignInRequest request)
    {
        return 
            signIn
                .run(request)
                .map(reply -> responseAssembler.toModel(reply));
    }
}
