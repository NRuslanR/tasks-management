package edu.examples.todos.features.clients.sign_in;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients/sign-in")
public class HttpSignInEndpoint extends AbstractSignInEndpoint
{
    public HttpSignInEndpoint(SignIn signIn, SignInResponseAssembler responseAssembler) {
        super(signIn, responseAssembler);
    }

    @Override
    @PostMapping
    public SignInResponse run(@RequestBody SignInRequest request)
    {
        return super.run(request);
    }
}
