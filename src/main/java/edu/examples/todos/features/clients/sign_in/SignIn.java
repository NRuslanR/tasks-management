package edu.examples.todos.features.clients.sign_in;

import edu.examples.todos.features.clients.get_client_info.ClientInfoNotFoundException;
import jakarta.validation.Valid;

public interface SignIn
{
    SignInReply run(@Valid SignInRequest request) throws NullPointerException, ClientInfoNotFoundException;
}
