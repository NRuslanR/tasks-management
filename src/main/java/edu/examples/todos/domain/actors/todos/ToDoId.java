package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.entities.identities.EntityId;
import edu.examples.todos.domain.common.entities.identities.PlainEntityId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;

@Data
public class ToDoId extends EntityId<ToDoId>
{
    @Column(name = "id")
    @NonNull
    private UUID value;

    public static ToDoId of(UUID value)
    {
        return new ToDoId(value);
    }

    public ToDoId(UUID value) throws NullPointerException
    {
        setValue(value);
    }

    protected ToDoId()
    {
        super();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ToDoId toDoId = (ToDoId) o;

        return value.equals(toDoId.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }
}
