package edu.examples.todos.features.clients.sign_up;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class AbstractSignUpEndpoint implements SignUpEndpoint
{
    private final SignUpService signUpService;
    private final SignUpResponseAssembler signUpResponseAssembler;

    @Override
    public Mono<SignUpResponse> signUp(SignUpRequest request)
    {
        return 
            signUpService
                .signUp(request)
                .map(signUpReply -> signUpResponseAssembler.toModel(signUpReply));
    }
}
