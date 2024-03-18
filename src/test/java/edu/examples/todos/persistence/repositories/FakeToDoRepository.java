package edu.examples.todos.persistence.repositories;

import edu.examples.todos.common.config.profiles.UnitTestsProfile;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.persistence.repositories.common.FakeEntityRepository;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@UnitTestsProfile
@Repository
public class FakeToDoRepository extends FakeEntityRepository<ToDo, ToDoId> implements ToDoRepository
{
    @Override
    public Optional<ToDo> findByName(String name)
    {
        return entities.stream().filter(e -> e.getName().equals(name)).findFirst();
    }
}
