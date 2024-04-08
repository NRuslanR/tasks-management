package edu.examples.todos.domain.resources.users;

import edu.examples.todos.domain.common.entities.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity<UserId>
{
    @Embedded
    private UserName name;

    @Embedded
    private UserClaims claims;
}
