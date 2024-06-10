package edu.examples.todos.features.clients.sign_up;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractSignUpEndpoint implements SignUpEndpoint
{
    private final SignUpService signUpService;
    private final SignUpResponseAssembler signUpResponseAssembler;

    @Override
    public SignUpResponse signUp(SignUpRequest request)
    {
        var signUpReply = signUpService.signUp(request);

        return signUpResponseAssembler.toModel(signUpReply);
    }
}
