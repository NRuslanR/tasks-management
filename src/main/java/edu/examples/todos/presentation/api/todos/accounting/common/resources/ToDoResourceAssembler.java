package edu.examples.todos.presentation.api.todos.accounting.common.resources;

import edu.examples.todos.presentation.api.common.resources.assemblers.LinkedRepresentationModelAssemblerSupport;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;

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
}
