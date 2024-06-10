package edu.examples.todos.usecases.todos.relationships.commands.assign_parent;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;

public class IncorrectAssignToDoParentCommandException extends UseCasesException
{
    public IncorrectAssignToDoParentCommandException() {
    }

    public IncorrectAssignToDoParentCommandException(String message) {
        super(message);
    }

    public IncorrectAssignToDoParentCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectAssignToDoParentCommandException(Throwable cause) {
        super(cause);
    }

    public IncorrectAssignToDoParentCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
