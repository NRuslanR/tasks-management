package edu.examples.todos.features.clients.sign_up;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clients/sign-up")
public class HttpSignUpEndpoint extends AbstractSignUpEndpoint
{
    public HttpSignUpEndpoint(SignUpService signUpService, SignUpResponseAssembler signUpResponseAssembler) {
        super(signUpService, signUpResponseAssembler);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SignUpResponse> signUp(@RequestBody SignUpRequest request)
    {
        return super.signUp(request);
    }
}
