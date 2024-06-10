package edu.examples.todos.features.clients.sign_up;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ClientAlreadyExistsException extends UseCasesException
{
    public ClientAlreadyExistsException() {
        super("Client is already exists");
    }

    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
