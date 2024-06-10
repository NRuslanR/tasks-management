package edu.examples.todos.presentation.api.features.todos.common.resources;

import edu.examples.todos.presentation.api.common.resources.assemblers.LinkedRepresentationModelAssemblerSupport;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import org.springframework.hateoas.Link;

import java.util.ArrayList;

public abstract class ToDoResourceAssembler extends LinkedRepresentationModelAssemblerSupport<ToDoDto, ToDoResource>
{
    public ToDoResourceAssembler(Class<?> controllerClass, Class<ToDoResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected ToDoResource instantiateModel(ToDoDto toDoDto)
    {
        return new ToDoResource(toDoDto);
    }

    @Override
    protected void setLinksToModel(ToDoResource resource, ToDoDto entity)
    {
        resource.add(createLinks(entity));
    }

    private Iterable<Link> createLinks(ToDoDto entity)
    {
        var actionsAvailability = entity.getActionsAvailability();

        var links = new ArrayList<Link>();

        links.add(createGetToDoByIdLink(entity));
        links.add(createGetToDoFullInfoByIdLink(entity));

        if (actionsAvailability.isChangingAvailable())
            links.add(createUpdateToDoLink(entity));

        if (actionsAvailability.isRemovingAvailable())
            links.add(createRemoveToDoLink(entity));

        if (actionsAvailability.isParentAssigningAvailable())
            links.add(createAssignToDoParentLink(entity));

        if (actionsAvailability.isPerformingAvailable())
            links.add(createPerformToDoLink(entity));

        return links;
    }

    protected abstract Link createGetToDoByIdLink(ToDoDto entity);

    protected abstract Link createGetToDoFullInfoByIdLink(ToDoDto entity);

    protected abstract Link createUpdateToDoLink(ToDoDto entity);

    protected abstract Link createRemoveToDoLink(ToDoDto entity);

    protected abstract Link createAssignToDoParentLink(ToDoDto entity);

    protected abstract Link createPerformToDoLink(ToDoDto entity);
}
