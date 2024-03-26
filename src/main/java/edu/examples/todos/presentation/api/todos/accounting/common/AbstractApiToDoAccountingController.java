package edu.examples.todos.presentation.api.todos.accounting.common;

import edu.examples.todos.presentation.api.common.config.ApiPaginationConfiguration;
import edu.examples.todos.presentation.api.todos.accounting.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.accounting.common.resources.ToDoResourceAssembler;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoResult;
import edu.examples.todos.usecases.todos.accounting.queries.ToDoAccountingQueryUseCases;
import edu.examples.todos.usecases.todos.accounting.queries.common.FindObjectsQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractApiToDoAccountingController implements ApiToDoAccountingController
{
    private final ToDoAccountingQueryUseCases toDoAccountingQueryUseCases;

    private final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    private final ToDoResourceAssembler toDoResourceAssembler;

    private final PagedResourcesAssembler<ToDoDto> pagedToDoResourceAssembler;

    private final ApiPaginationConfiguration paginationConfig;

    public Mono<PagedModel<ToDoResource>> findToDos(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<List<String>> sort
    )
    {
        var findObjectsQuery =
                FindObjectsQuery
                        .builder()
                        .withPagination(
                                page,
                                paginationConfig.getDefaultPage(),
                                size,
                                paginationConfig.getDefaultPageSize(),
                                sort
                        )
                        .build();

        var query = new FindToDosQuery(findObjectsQuery);

        return
                toDoAccountingQueryUseCases
                        .findToDos(query)
                        .map(FindToDosResult::getToDoPage)
                        .flatMap(this::toPagedResource);
    }

    private Mono<PagedModel<ToDoResource>> toPagedResource(Page<ToDoDto> toDoDtoPage)
    {
        return Mono.fromCallable(() -> pagedToDoResourceAssembler.toModel(toDoDtoPage, toDoResourceAssembler));
    }

    private Mono<Page<ToDoResource>> toResources(Page<ToDoDto> toDoPage)
    {
        return Mono.fromCallable(() ->
                new PageImpl<ToDoResource>(
                        toDoPage.getContent().stream().map(toDoResourceAssembler::toModel).toList(),
                        toDoPage.getPageable(),
                        toDoPage.getTotalElements()
                )
        );
    }

    public Mono<ToDoResource> getToDoById(String toDoId)
    {
        return
                toDoAccountingQueryUseCases
                        .getToDoById(new GetByIdQuery(toDoId))
                        .map(GetByIdResult::getToDo)
                        .flatMap(this::toResource);
    }

    public Mono<ToDoResource> createToDo(CreateToDoCommand createToDoCommand)
    {
        return
                toDoAccountingCommandUseCases
                        .createToDo(createToDoCommand)
                        .map(CreateToDoResult::getToDo)
                        .flatMap(this::toResource);
    }

    @Override
    public Mono<ToDoResource> updateToDo(String toDoId, UpdateToDoCommand updateToDoCommand)
    {
        updateToDoCommand.setToDoId(toDoId);

        return
                toDoAccountingCommandUseCases
                        .updateToDo(updateToDoCommand)
                        .map(UpdateToDoResult::getToDo)
                        .flatMap(this::toResource);
    }

    private Mono<ToDoResource> toResource(ToDoDto toDoDto)
    {
        return Mono.fromCallable(() -> toDoResourceAssembler.toModel(toDoDto));
    }
}
