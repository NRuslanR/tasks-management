package edu.examples.todos.persistence.repositories;

import edu.examples.todos.persistence.common.config.IntegrationPersistenceTest;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationPersistenceTest
public class IntegrationToDoRepositoryTests extends ToDoRepositoryTests
{
    @Autowired
    public IntegrationToDoRepositoryTests(ToDoRepository entityRepository) {
        super(entityRepository);
    }
}
