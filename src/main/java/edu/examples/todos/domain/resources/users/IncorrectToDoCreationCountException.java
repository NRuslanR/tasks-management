package edu.examples.todos.domain.resources.users;

public class IncorrectToDoCreationCountException extends UserException
{
    public IncorrectToDoCreationCountException()
    {
    }

    public IncorrectToDoCreationCountException(String message)
    {
        super(message);
    }
}
