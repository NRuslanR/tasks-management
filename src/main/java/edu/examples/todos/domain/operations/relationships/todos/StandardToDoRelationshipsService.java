package edu.examples.todos.domain.operations.relationships.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoList;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoRelationshipsService implements ToDoRelationshipsService
{
    private final ToDoFinder toDoFinder;

    @Override
    public Mono<ToDo> assignToDoParentAsync(ToDo targetToDo, ToDo parentToDo)
            throws NullPointerException, DescendentToDoCanNotBeParentForAncestorToDoException
    {
        ensureToDosValid(targetToDo, parentToDo);

        return doAssignToDoParentAsync(targetToDo, parentToDo);
    }

    private void ensureToDosValid(ToDo targetToDo, ToDo parentToDo)
    {
        Mono.fromCallable(() ->
        {
            Objects.requireNonNull(targetToDo);

            return Objects.requireNonNull(parentToDo);

        }).block();
    }

    private Mono<ToDo> doAssignToDoParentAsync(
            ToDo targetToDo, ToDo parentToDo
    )
    {
        if (targetToDo.equals(parentToDo))
        {
            return Mono.error(new DescendentToDoCanNotBeParentForAncestorToDoException(
                "To-Do can't be parent for itself"
            ));
        }

        return
                toDoFinder
                        .findAllSubToDosRecursivelyForAsync(targetToDo)
                        .map(v -> {

                            throwIfListIncludesToDo(v, parentToDo);

                            return targetToDo;
                        })
                        .map(v -> {

                            v.setParentToDoId(parentToDo.getId());

                            return v;
                        });
    }

    private void throwIfListIncludesToDo(ToDoList toDoList, ToDo parentToDo)
    {
        if (toDoList.contains(parentToDo))
        {
            throw new DescendentToDoCanNotBeParentForAncestorToDoException();
        }
    }
}
