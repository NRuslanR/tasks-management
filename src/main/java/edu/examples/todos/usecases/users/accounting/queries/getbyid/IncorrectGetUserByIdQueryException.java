package edu.examples.todos.usecases.users.accounting.queries.getbyid;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectGetUserByIdQueryException extends UseCasesException
{
    public IncorrectGetUserByIdQueryException() {
        super("Incorrect query to get user by id");
    }

    public IncorrectGetUserByIdQueryException(String message) {
        super(message);
    }
}
