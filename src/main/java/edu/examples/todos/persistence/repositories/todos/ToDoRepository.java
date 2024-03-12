package edu.examples.todos.persistence.repositories.todos;


import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.persistence.repositories.common.EntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends EntityRepository<ToDo, ToDoId>
{
}
