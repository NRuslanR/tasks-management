package edu.examples.todos.persistence.repositories;

import edu.examples.todos.domain.actors.ToDoTestsUtils;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.persistence.common.PersistenceTests;
import edu.examples.todos.persistence.repositories.common.DomainEntityRepositoryTests;
import edu.examples.todos.persistence.repositories.common.EntityRepository;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoRepositoryTests extends DomainEntityRepositoryTests<ToDoRepository, ToDo, ToDoId>
{
    @Autowired
    public ToDoRepositoryTests(ToDoRepository entityRepository)
    {
        super(entityRepository);
    }

    @Override
    protected List<ToDo> createSeedEntities(EntityManager entityManager, TransactionTemplate transactionTemplate)
    {
        return
            transactionTemplate.execute(t -> {

                var toDos =
                        ToDoTestsUtils
                                .createSimpleTestToDos("ToDo#1", "ToDo#2", "ToDo#3");

                toDos.forEach(entityManager::persist);

                return toDos;
            });
    }

    @Override
    protected ToDoId createNonExistentEntityId()
    {
        return ToDoId.of(UUID.randomUUID());
    }

    @Override
    protected ToDo createEntityToBeAdded()
    {
        return ToDoTestsUtils.createSimpleTestToDo("ToDo#Test#1");
    }

    @Override
    protected void changeEntityToBeUpdated(ToDo target)
    {
        target.setDescription("Changed description");
    }

    @Override
    protected void assertEntitiesEquals(ToDo expected, ToDo actual)
    {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreatedAt().withNano(0), actual.getCreatedAt().withNano(0));
    }
}
