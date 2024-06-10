package edu.examples.todos.domain.resources.users;

import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Embeddable
public class UserClaims
{
    int allowedToDoCreationCount;
    boolean canPerformForeignTodos;
    boolean canEditForeignTodos;
    boolean canRemoveForeignTodos;

    protected UserClaims()
    {
        allowedToDoCreationCount = 0;
        canPerformForeignTodos = false;
        canEditForeignTodos = false;
        canRemoveForeignTodos = false;
    }

    public static UserClaims unbounded()
    {
        return UserClaims.of(Integer.MAX_VALUE, true, true, true);
    }

    public static UserClaims of(
            int allowedToDoCreationCount,
            boolean canEditForeignTodos,
            boolean canRemoveForeignTodos,
            boolean canPerformForeignTodos
    )
    {
        return new UserClaims(allowedToDoCreationCount, canEditForeignTodos, canRemoveForeignTodos, canPerformForeignTodos);
    }

    private UserClaims(
            int allowedToDoCreationCount,
            boolean canEditForeignTodos,
            boolean canRemoveForeignTodos,
            boolean canPerformForeignTodos
    )
    {
        if (allowedToDoCreationCount < 0)
            throw new IncorrectToDoCreationCountException("To-Do creation count limit can't be negative");

        this.allowedToDoCreationCount = allowedToDoCreationCount;
        this.canEditForeignTodos = canEditForeignTodos;
        this.canRemoveForeignTodos = canRemoveForeignTodos;
        this.canPerformForeignTodos = canPerformForeignTodos;
    }

    public UserClaims changeAllowedToDoCreationCount(int value)
    {
        return UserClaims.of(value, canPerformForeignTodos, canEditForeignTodos, canRemoveForeignTodos);
    }

    public UserClaims canPerformForeignTodos(boolean value)
    {
        return UserClaims.of(allowedToDoCreationCount, value, canEditForeignTodos, canRemoveForeignTodos);
    }

    public UserClaims canEditForeignTodos(boolean value)
    {
        return UserClaims.of(allowedToDoCreationCount, canPerformForeignTodos, value, canRemoveForeignTodos);
    }

    public UserClaims canRemoveForeignTodos(boolean value)
    {
        return UserClaims.of(allowedToDoCreationCount, canPerformForeignTodos, canEditForeignTodos, value);
    }
}
