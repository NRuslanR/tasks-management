package edu.examples.todos.presentation.api.todos.common.resources;

import edu.examples.todos.usecases.todos.common.dtos.ToDoFullInfoDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public abstract class ToDoFullInfoResourceAssembler
        extends RepresentationModelAssemblerSupport<ToDoFullInfoDto, ToDoFullInfoResource>
{
    private final ToDoResourceAssembler toDoResourceAssembler;

    public ToDoFullInfoResourceAssembler(
            ToDoResourceAssembler toDoResourceAssembler,
            Class<?> controllerClass,
            Class<ToDoFullInfoResource> resourceType
    ) {
        super(controllerClass, resourceType);

        this.toDoResourceAssembler = toDoResourceAssembler;
    }

    @Override
    public ToDoFullInfoResource toModel(ToDoFullInfoDto entity)
    {
        return new ToDoFullInfoResource(
                toDoResourceAssembler.toModel(entity.getToDo()),
                entity.getSubToDos().stream().map(this::toModel).toList()
        );
    }
}
