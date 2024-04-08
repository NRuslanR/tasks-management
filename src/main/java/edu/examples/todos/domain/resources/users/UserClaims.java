package edu.examples.todos.domain.resources.users;

import jakarta.persistence.Embeddable;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "of")
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
