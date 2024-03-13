package edu.examples.todos.persistence.repositories.todos;


import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.persistence.repositories.common.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoRepository extends EntityRepository<ToDo, ToDoId>
{
    Optional<ToDo> findByName(String name);
}
