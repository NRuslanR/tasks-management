package edu.examples.todos.presentation.api.features.todos.relationships.web;

import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.presentation.api.features.todos.relationships.AbstractApiToDoRelationshipsController;
import edu.examples.todos.usecases.todos.relationships.commands.ToDoRelationshipsCommandUseCases;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/todos")
@CrossOrigin(origins = "*")
public class HttpApiToDoRelationshipsController extends AbstractApiToDoRelationshipsController
{
    public HttpApiToDoRelationshipsController(
            ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases,
            ToDoResourceAssembler toDoResourceAssembler
    )
    {
        super(toDoRelationshipsCommandUseCases, toDoResourceAssembler);
    }

    @Override
    @PostMapping(path = "/{toDoId}/assign-parent")
    public Mono<ToDoResource> assignToDoParent(
            @PathVariable("toDoId") String targetToDoId,
            @RequestBody AssignToDoParentCommand command
    )
    {
        return super.assignToDoParent(targetToDoId, command);
    }
}
