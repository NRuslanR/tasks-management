package edu.examples.todos.presentation.api.todos.relationships;

import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.relationships.commands.ToDoRelationshipsCommandUseCases;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentResult;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class AbstractApiToDoRelationshipsController implements ApiToDoRelationshipsController
{
    private final ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases;

    private final ToDoResourceAssembler toDoResourceAssembler;

    @Override
    public Mono<ToDoResource> assignToDoParent(String targetToDoId, AssignToDoParentCommand command)
    {
        return
                toDoRelationshipsCommandUseCases
                        .assignToDoParent(command)
                        .map(AssignToDoParentResult::getToDo)
                        .flatMap(this::toResource);
    }

    private Mono<ToDoResource> toResource(ToDoDto toDoDto)
    {
        return Mono.fromCallable(() -> toDoResourceAssembler.toModel(toDoDto));
    }
}
