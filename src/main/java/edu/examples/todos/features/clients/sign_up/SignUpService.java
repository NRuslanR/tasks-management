package edu.examples.todos.features.clients.sign_up;

import jakarta.validation.Valid;

public interface SignUpService
{
    SignUpReply signUp(@Valid SignUpRequest request) throws NullPointerException, ClientAlreadyExistsException;
}
