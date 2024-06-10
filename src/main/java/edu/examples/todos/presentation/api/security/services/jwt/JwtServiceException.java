package edu.examples.todos.presentation.api.security.services.jwt;

public class JwtServiceException extends RuntimeException
{
    public JwtServiceException() {
    }

    public JwtServiceException(String message) {
        super(message);
    }
}
