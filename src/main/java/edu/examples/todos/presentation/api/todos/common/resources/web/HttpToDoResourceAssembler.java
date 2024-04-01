package edu.examples.todos.presentation.api.todos.common.resources.web;

import edu.examples.todos.presentation.api.todos.accounting.web.HttpApiToDoAccountingController;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.presentation.api.todos.relationships.web.HttpApiToDoRelationshipsController;
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
            .withSelfRel().toMono().block(),
            linkTo(
                    methodOn(HttpApiToDoAccountingController.class).updateToDo(toDoDto.getId(), null)
            ).withRel("update").toMono().block(),
            linkTo(
                    methodOn(HttpApiToDoAccountingController.class).removeToDo(toDoDto.getId())
            ).withRel("remove").toMono().block(),
            linkTo(
                    methodOn(HttpApiToDoRelationshipsController.class).assignToDoParent(toDoDto.getId(), null)
            ).withRel("assign-parent").toMono().block()
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
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty()
                    )
            ).withSelfRel().toMono().block().expand()
        );
    }
}
