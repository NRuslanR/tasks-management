package edu.examples.todos.domain.resources.users;

public class IncorrectUserNameException extends UserException
{
    public IncorrectUserNameException() {
    }

    public IncorrectUserNameException(String message) {
        super(message);
    }
}
