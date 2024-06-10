package edu.examples.todos.persistence.repositories.common;

import edu.examples.todos.domain.common.entities.BaseEntity;
import edu.examples.todos.domain.common.entities.identities.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository<Entity extends BaseEntity<Id>, Id extends EntityId<Id>>
        extends JpaRepository<Entity, Id>
{
}
