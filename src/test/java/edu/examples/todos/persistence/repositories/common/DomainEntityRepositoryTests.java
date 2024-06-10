package edu.examples.todos.persistence.repositories.common;

import edu.examples.todos.domain.common.entities.BaseEntity;
import edu.examples.todos.domain.common.entities.identities.EntityId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class DomainEntityRepositoryTests<
        Repository extends EntityRepository<Entity, Identity>,
        Entity extends BaseEntity<Identity>, 
        Identity extends EntityId<Identity>
    >
{
    protected List<Entity> seedEntities;

    protected final Repository entityRepository;

    @BeforeAll
    public void setupFixtureForAll()
    {
        seedEntities = createSeedEntities();

        entityRepository.saveAll(seedEntities);
    }

    @AfterAll
    public void clearFixtureForAll()
    {
        entityRepository.deleteAll();
    }

    protected abstract List<Entity> createSeedEntities();

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
        var expected = createTestEntity();

        var actual = entityRepository.save(expected);

        assertEntitiesEquals(expected, actual);
    }

    @Test
    public void should_Update_Entity_When_ItExists()
    {
        var expected = createTestEntity();

        expected = entityRepository.save(expected);

        changeEntityToBeUpdated(expected);

        var actual = entityRepository.save(expected);

        assertEntitiesEquals(expected, actual);
    }

    protected abstract void changeEntityToBeUpdated(Entity target);

    protected abstract Entity createTestEntity();

    @Test
    public void should_Remove_Entity_When_EntityExists()
    {
        var entity = createTestEntity();

        entity = entityRepository.save(entity);

        entityRepository.delete(entity);

        var result = entityRepository.findById(entity.getId());

        assertTrue(result.isEmpty());
    }

    protected abstract void assertEntitiesEquals(Entity expected, Entity actual);
}
