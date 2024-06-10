package edu.examples.todos.features.clients.get_client_info;

import edu.examples.todos.features.shared.FeatureException;

public class ClientInfoNotFoundException extends FeatureException
{
    public ClientInfoNotFoundException() {
        super("Client info not found");
    }

    public ClientInfoNotFoundException(String message) {
        super(message);
    }
}
