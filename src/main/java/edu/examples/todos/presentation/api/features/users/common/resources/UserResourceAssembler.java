package edu.examples.todos.presentation.api.features.users.common.resources;

import edu.examples.todos.presentation.api.common.resources.assemblers.LinkedRepresentationModelAssemblerSupport;
import edu.examples.todos.usecases.users.accounting.UserDto;

public abstract class UserResourceAssembler extends LinkedRepresentationModelAssemblerSupport<UserDto, UserResource>
{
    public UserResourceAssembler(Class<?> controllerClass, Class<UserResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected UserResource instantiateModel(UserDto entity)
    {
        return new UserResource(entity);
    }
}
