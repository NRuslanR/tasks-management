package edu.examples.todos.presentation.api.users.common.resources.web;

import edu.examples.todos.presentation.api.users.accounting.web.HttpApiUserAccountingController;
import edu.examples.todos.presentation.api.users.common.resources.UserResource;
import edu.examples.todos.presentation.api.users.common.resources.UserResourceAssembler;
import edu.examples.todos.usecases.users.accounting.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

@Component
public class HttpUserResourceAssembler extends UserResourceAssembler
{
    public HttpUserResourceAssembler()
    {
        super(HttpApiUserAccountingController.class, UserResource.class);
    }

    @Override
    protected void setLinksToModel(UserResource resource, UserDto entity)
    {
        super.setLinksToModel(resource, entity);
    }

    @Override
    protected void setLinksToCollectionModel(
            CollectionModel<UserResource> resources,
            Iterable<? extends UserDto> entities
    )
    {
        super.setLinksToCollectionModel(resources, entities);
    }
}
