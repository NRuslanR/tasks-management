package edu.examples.todos.presentation.api.features.todos.relationships;

import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResource;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import reactor.core.publisher.Mono;

public interface ApiToDoRelationshipsController
{
    Mono<ToDoResource> assignToDoParent(String targetToDoId, AssignToDoParentCommand command);
}
