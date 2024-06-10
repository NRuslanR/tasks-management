package edu.examples.todos.features.clients.sign_in;

import jakarta.validation.Valid;

public interface SignInEndpoint
{
    SignInResponse run(@Valid SignInRequest request);
}
