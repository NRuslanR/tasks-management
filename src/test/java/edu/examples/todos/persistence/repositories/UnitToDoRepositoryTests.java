package edu.examples.todos.persistence.repositories;

import edu.examples.todos.persistence.common.config.UnitPersistenceTest;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;

@UnitPersistenceTest
public class UnitToDoRepositoryTests extends ToDoRepositoryTests
{
    @Autowired
    public UnitToDoRepositoryTests(ToDoRepository entityRepository) {
        super(entityRepository);
    }
}
