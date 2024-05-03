package edu.examples.todos.persistence.repositories.todos;

import edu.examples.todos.common.config.profiles.DefaultProfile;
import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.persistence.repositories.common.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@DefaultProfile
public interface ToDoRepository extends EntityRepository<ToDo, ToDoId>
{
    Optional<ToDo> findByName(String name);

    @Query(
            value = "with recursive get_all_sub_todos(id) as (" +
                    "select id from todos where parentToDoId = :todo_id " +
                    "union " +
                    "select t.id from todos t " +
                    "join get_all_sub_todos st on t.parentToDoId = st.id " +
            ") select t.* from get_all_sub_todos st " +
            "join todos t on st.id = t.id",
            nativeQuery = true
    )
    List<ToDo> findAllSubToDosRecursivelyFor(@Param("todo_id") UUID toDoId);

    default OperableToDo save(OperableToDo toDo)
    {
        return OperableToDo.of(save(toDo.getTarget()), toDo.getActionsAvailability());
    }

    default OperableToDo delete(OperableToDo toDo)
    {
        delete(toDo.getTarget());

        return toDo;
    }

    default OperableToDo deleteRecursively(OperableToDo toDo)
    {
        deleteRecursively(toDo.getTarget());

        return toDo;
    }

    default void deleteRecursively(ToDo toDo)
    {
        var subToDos = findAllSubToDosRecursivelyFor(toDo.getId().getValue());

        deleteAll(subToDos);
        delete(toDo);
    }
}
