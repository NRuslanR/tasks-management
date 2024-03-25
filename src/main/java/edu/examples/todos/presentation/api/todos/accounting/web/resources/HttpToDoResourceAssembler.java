package edu.examples.todos.presentation.api.todos.accounting.web.resources;

import edu.examples.todos.presentation.api.todos.accounting.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.accounting.common.resources.ToDoResourceAssembler;
import edu.examples.todos.presentation.api.todos.accounting.web.HttpApiToDoAccountingController;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class HttpToDoResourceAssembler extends ToDoResourceAssembler
{
    public HttpToDoResourceAssembler()
    {
        super(HttpApiToDoAccountingController.class, ToDoResource.class);
    }

    @Override
    protected void setLinksToModel(ToDoResource resource, ToDoDto toDoDto)
    {
        resource.add(
            linkTo(
                    methodOn(HttpApiToDoAccountingController.class).getToDoById(toDoDto.getId())
            )
            .withSelfRel().toMono().block()
        );
    }

    @Override
    protected void setLinksToCollectionModel(
            CollectionModel<ToDoResource> resources,
            Iterable<? extends ToDoDto> entities
    )
    {
        resources.add(
            linkTo(
                    methodOn(
                            HttpApiToDoAccountingController.class
                    ).findToDos(
                            Optional.empty(), Optional.empty(),
                            Optional.empty()
                    )
            ).withSelfRel().toMono().block().expand()
        );
    }
}