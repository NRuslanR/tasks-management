package edu.examples.todos.domain.common.entities;

import edu.examples.todos.domain.common.entities.identities.EntityId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@Data
@MappedSuperclass
public abstract class BaseEntity<Identity extends EntityId<Identity>>
{
    @EmbeddedId
    @NonNull
    @Setter(AccessLevel.PRIVATE)
    private Identity id;

    protected BaseEntity(Identity id) throws NullPointerException
    {
        setId(id);
    }

    protected BaseEntity()
    {

    }

    @Override
    public boolean equals(Object o)
    {
        return
                o != null &&
                o.getClass() == getClass() &&
                ((BaseEntity<?>)o).getId().equals(getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
