package edu.examples.todos.domain.common.entities.identities;

import lombok.Data;

import java.util.Objects;

@Data
public class PlainEntityId<T, V extends PlainEntityId<T, V>> extends EntityId<V>
{
    private T value;

    public PlainEntityId(T value) throws NullPointerException
    {
        Objects.requireNonNull(value);
    }

    protected PlainEntityId()
    {

    }

    @Override
    public boolean equals(Object other)
    {
        return
                other instanceof PlainEntityId &&
                getValue().equals(((PlainEntityId<?, ?>)other).getValue());
    }
}
