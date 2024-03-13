package edu.examples.todos.persistence.repositories.common;

import edu.examples.todos.domain.common.entities.BaseEntity;
import edu.examples.todos.domain.common.entities.identities.EntityId;
import edu.examples.todos.persistence.common.PersistenceTests;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public abstract class DomainEntityRepositoryTests<
        Repository extends EntityRepository<Entity, Identity>,
        Entity extends BaseEntity<Identity>, 
        Identity extends EntityId<Identity>
    > extends PersistenceTests
{
    protected List<Entity> seedEntities;

    protected final Repository entityRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setupForEach()
    {
        if (Objects.isNull(seedEntities))
            seedEntities = createSeedEntities(entityManager, transactionTemplate);
    }

    protected abstract List<Entity> createSeedEntities(EntityManager entityManager, TransactionTemplate transactionTemplate);

    @Test
    public void should_Return_EntityById_When_ItExists()
    {
        var expected = seedEntities.get(0);

        var actual = entityRepository.findById(expected.getId());

        assertTrue(actual.isPresent());

        assertEntitiesEquals(expected, actual.get());
    }

    @Test
    public void should_Return_NullEntityById_When_ItDoesNotExists()
    {
        Identity id = createNonExistentEntityId();

        var actual = entityRepository.findById(id);

        assertTrue(actual.isEmpty());
    }

    protected abstract Identity createNonExistentEntityId();

    @Test
    public void should_Add_Entity_When_EntityIsValid()
    {
        var expected = createEntityToBeAdded();

        var actual = entityRepository.save(expected);

        assertEntitiesEquals(expected, actual);
    }

    @Test
    public void should_Update_Entity_When_ItExists()
    {
        var expected = seedEntities.get(0);

        changeEntityToBeUpdated(expected);

        var actual = entityRepository.save(expected);

        assertEntitiesEquals(expected, actual);
    }

    protected abstract void changeEntityToBeUpdated(Entity target);

    protected abstract Entity createEntityToBeAdded();

    @Test
    public void should_Remove_Entity_When_EntityExists()
    {
        var id = seedEntities.get(0).getId();

        var actual = entityRepository.findById(id).get();

        entityRepository.delete(actual);

        var result = entityRepository.findById(id);

        assertTrue(result.isEmpty());
    }

    protected abstract void assertEntitiesEquals(Entity expected, Entity actual);
}
