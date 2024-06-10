package edu.examples.todos.domain.operations.relationships.todos;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import reactor.core.publisher.Mono;

public interface ToDoRelationshipsService
{
    Mono<OperableToDo> assignToDoParentAsync(ToDoId targetToDoId, ToDoId parentToDoId)
            throws NullPointerException, DescendentToDoCanNotBeParentForAncestorToDoException;
}
