package edu.examples.todos.domain.operations.relationships.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import reactor.core.publisher.Mono;

public interface ToDoRelationshipsService
{
    Mono<ToDo> assignToDoParentAsync(ToDo targetToDo, ToDo parentToDo)
            throws NullPointerException, DescendentToDoCanNotBeParentForAncestorToDoException;
}
