package edu.examples.todos.persistence.repositories.common;

import edu.examples.todos.domain.common.entities.BaseEntity;
import edu.examples.todos.domain.common.entities.identities.EntityId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class FakeEntityRepository<Entity extends BaseEntity<Id>, Id extends EntityId<Id>>
        implements EntityRepository<Entity, Id>
{
    protected Set<Entity> entities = new HashSet<>();

    @Override
    public void flush()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> S saveAndFlush(S entity)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> List<S> saveAllAndFlush(Iterable<S> entities)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch(Iterable<Entity> entities)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Id> ids)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getOne(Id id)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getById(Id id)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getReferenceById(Id id)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> Optional<S> findOne(Example<S> example)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> List<S> findAll(Example<S> example)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> List<S> findAll(Example<S> example, Sort sort)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> Page<S> findAll(Example<S> example, Pageable pageable)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> long count(Example<S> example)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> boolean exists(Example<S> example)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Entity> S save(S entity)
    {
        entities.remove(entity);
        entities.add(entity);

        return entity;
    }

    @Override
    public <S extends Entity> List<S> saveAll(Iterable<S> entities)
    {
        entities.forEach(this::save);

        return IterableUtils.toList(entities);
    }

    @Override
    public Optional<Entity> findById(Id id)
    {
        return entities.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Id id)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Entity> findAll()
    {
        return entities.stream().toList();
    }

    @Override
    public List<Entity> findAllById(Iterable<Id> ids)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Id id)
    {
        var entity = findById(id);

        entity.ifPresent(entities::remove);
    }

    @Override
    public void delete(Entity entity)
    {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Id> ids)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Entity> entities)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll()
    {
        entities.clear();
    }

    @Override
    public List<Entity> findAll(Sort sort)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Entity> findAll(Pageable pageable)
    {
        throw new UnsupportedOperationException();
    }
}
