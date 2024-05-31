package edu.examples.todos.features.clients.sign_up;

import org.springframework.stereotype.Component;

@Component
public class HttpSignUpResponseAssembler extends SignUpResponseAssembler
{
    public HttpSignUpResponseAssembler()
    {
        super(HttpSignUpEndpoint.class, SignUpResponse.class);
    }
}
