package edu.examples.todos.features.clients.sign_in;

import org.springframework.stereotype.Component;

@Component
public class HttpSignInResponseAssembler extends SignInResponseAssembler
{
    public HttpSignInResponseAssembler()
    {
        super(HttpSignInEndpoint.class, SignInResponse.class);
    }
}
