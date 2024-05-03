package edu.examples.todos.presentation.api.todos.common.resources.web;

import edu.examples.todos.presentation.api.todos.accounting.web.HttpApiToDoAccountingController;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.presentation.api.todos.relationships.web.HttpApiToDoRelationshipsController;
import edu.examples.todos.presentation.api.todos.workcycle.performing.web.HttpApiToDoPerformingController;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
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
    protected Link createGetToDoByIdLink(ToDoDto entity)
    {
        return linkTo(
                methodOn(HttpApiToDoAccountingController.class).getToDoById(entity.getId())
        ).withSelfRel().toMono().block();
    }

    @Override
    protected Link createGetToDoFullInfoByIdLink(ToDoDto entity)
    {
        return linkTo(
                methodOn(HttpApiToDoAccountingController.class).getToDoFullInfoById(entity.getId())
        ).withRel("full-info").toMono().block();
    }

    @Override
    protected Link createUpdateToDoLink(ToDoDto entity)
    {
        return linkTo(
                methodOn(HttpApiToDoAccountingController.class).updateToDo(entity.getId(), null)
        ).withRel("update").toMono().block();
    }

    @Override
    protected Link createRemoveToDoLink(ToDoDto entity)
    {
        return linkTo(
                methodOn(HttpApiToDoAccountingController.class).removeToDo(entity.getId())
        ).withRel("remove").toMono().block();
    }

    @Override
    protected Link createAssignToDoParentLink(ToDoDto entity)
    {
        return linkTo(
                methodOn(HttpApiToDoRelationshipsController.class).assignToDoParent(entity.getId(), null)
        ).withRel("assign-parent").toMono().block();
    }

    @Override
    protected Link createPerformToDoLink(ToDoDto entity)
    {
        return linkTo(
                methodOn(HttpApiToDoPerformingController.class).performToDo(entity.getId())
        ).withRel("perform").toMono().block();
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
