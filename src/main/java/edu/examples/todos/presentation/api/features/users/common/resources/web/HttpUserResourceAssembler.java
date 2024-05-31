package edu.examples.todos.presentation.api.features.users.common.resources.web;

import edu.examples.todos.presentation.api.features.users.accounting.web.HttpApiUserAccountingController;
import edu.examples.todos.presentation.api.features.users.common.resources.UserResource;
import edu.examples.todos.presentation.api.features.users.common.resources.UserResourceAssembler;
import edu.examples.todos.usecases.users.accounting.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class HttpUserResourceAssembler extends UserResourceAssembler
{
    public HttpUserResourceAssembler()
    {
        super(HttpApiUserAccountingController.class, UserResource.class);
    }

    @Override
    protected void setLinksToModel(
            UserResource resource,
            UserDto entity
    )
    {
        resource.add(
            linkTo(
                methodOn(HttpApiUserAccountingController.class).getUserById(entity.getId())
            ).withSelfRel().toMono().block(),
            linkTo(
                methodOn(HttpApiUserAccountingController.class).removeUser(entity.getId())
            ).withRel("remove").toMono().block()
        );
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
