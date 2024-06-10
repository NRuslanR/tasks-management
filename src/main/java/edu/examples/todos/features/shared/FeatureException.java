package edu.examples.todos.features.shared;

public class FeatureException extends RuntimeException
{
    public FeatureException() {
    }

    public FeatureException(String message) {
        super(message);
    }
}
