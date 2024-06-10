package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.entities.identities.EntityId;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Objects;
import java.util.UUID;

@Data
public class ToDoId extends EntityId<ToDoId>
{
    @NonNull
    @Column(name = "id")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID value;

    public static ToDoId of(String value)
    {
        return of(UUID.fromString(value));
    }

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

    public String asString()
    {
        return value.toString();
    }
}
