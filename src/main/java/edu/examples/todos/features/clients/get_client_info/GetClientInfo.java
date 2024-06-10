package edu.examples.todos.features.clients.get_client_info;

import jakarta.validation.Valid;

public interface GetClientInfo
{
    GetClientInfoResult run(@Valid GetClientInfoQuery query) throws NullPointerException, ClientInfoNotFoundException;
}
