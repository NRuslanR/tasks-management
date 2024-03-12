package edu.examples.todos.domain.common.entities.identities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import java.io.Serializable;

@Embeddable
public abstract class EntityId<T extends EntityId<T>> implements Serializable
{
    public boolean equals(T other)
    {
        return super.equals((Object)other);
    }
}
