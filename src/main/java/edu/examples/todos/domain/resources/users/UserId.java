package edu.examples.todos.domain.resources.users;

import edu.examples.todos.domain.common.entities.identities.EntityId;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class UserId extends EntityId<UserId>
{
    @NonNull
    @Column(name = "id")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID value;

    public static UserId of(String userId)
    {
        return of(UUID.fromString(userId));
    }

    public static UserId of(UUID value)
    {
        return new UserId(value);
    }

    public UserId(UUID value)
    {
        setValue(value);
    }

    protected UserId()
    {

    }
}
