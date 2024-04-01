package edu.examples.todos.domain.operations.relationships.todos;

import edu.examples.todos.domain.common.exceptions.DomainException;

public class DescendentToDoCanNotBeParentForAncestorToDoException extends DomainException
{
    public DescendentToDoCanNotBeParentForAncestorToDoException()
    {
        super("The descendent To-Do can't be parent for ancestor To-Do");
    }

    public DescendentToDoCanNotBeParentForAncestorToDoException(String message) {
        super(message);
    }

    public DescendentToDoCanNotBeParentForAncestorToDoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DescendentToDoCanNotBeParentForAncestorToDoException(Throwable cause) {
        super(cause);
    }

    public DescendentToDoCanNotBeParentForAncestorToDoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
