package edu.examples.todos.domain.resources.users;

import edu.examples.todos.domain.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity<UserId>
{
    @Embedded
    @Delegate
    private UserName name;

    @Embedded
    @Delegate
    private UserClaims claims;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist()
    {
        createdAt = LocalDateTime.now().withNano(0);
    }

    public static User of(UserId id, UserName name, UserClaims claims)
    {
        return new User(id, name,  claims);
    }

    private User(UserId id, UserName name, UserClaims claims)
    {
        super(id);

        setName(name);
        setClaims(claims);
    }

    protected User()
    {
    }
}
