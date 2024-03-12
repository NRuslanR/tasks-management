package edu.examples.todos.domain.common.entities;

import com.fasterxml.jackson.databind.ser.Serializers;
import edu.examples.todos.domain.common.entities.identities.EntityId;
import edu.examples.todos.domain.common.entities.identities.PlainEntityId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.MapsId;
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
