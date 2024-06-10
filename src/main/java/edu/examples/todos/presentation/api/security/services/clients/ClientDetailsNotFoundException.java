package edu.examples.todos.presentation.api.security.services.clients;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class ClientDetailsNotFoundException extends UseCasesException
{
    public static final String MESSAGE_CONTENT = "Client details not found";

    public ClientDetailsNotFoundException() {
        super(MESSAGE_CONTENT);
    }

    public ClientDetailsNotFoundException(String message) {
        super(message);
    }
}
