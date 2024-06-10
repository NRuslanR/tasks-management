package edu.examples.todos.persistence.repositories;

import edu.examples.todos.common.config.profiles.UnitTestsProfile;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.persistence.repositories.common.FakeEntityRepository;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UnitTestsProfile
@Repository
public class FakeToDoRepository extends FakeEntityRepository<ToDo, ToDoId> implements ToDoRepository
{
    @Override
    public Optional<ToDo> findByName(String name)
    {
        return entities.stream().filter(e -> e.getName().equals(name)).findFirst();
    }

    @Override
    public List<ToDo> findAllSubToDosRecursivelyFor(UUID toDoId)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long countByAuthorId(UserId authorId)
    {
        return entities.stream().map(ToDo::getAuthor).map(User::getId).filter(authorId::equals).count();
    }
}
