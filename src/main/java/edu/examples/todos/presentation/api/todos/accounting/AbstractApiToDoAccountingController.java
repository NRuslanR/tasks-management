package edu.examples.todos.presentation.api.todos.accounting;

import edu.examples.todos.presentation.api.common.config.ApiPaginationConfiguration;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoFullInfoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoFullInfoResourceAssembler;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.ToDoFullInfoDto;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoResult;
import edu.examples.todos.usecases.todos.accounting.queries.ToDoAccountingQueryUseCases;
import edu.examples.todos.usecases.todos.accounting.queries.common.FindObjectsQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosResult;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdResult;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
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

    private final ToDoFullInfoResourceAssembler toDoFullInfoResourceAssembler;

    private final PagedResourcesAssembler<ToDoDto> pagedToDoResourceAssembler;

    private final ApiPaginationConfiguration paginationConfig;

    @Override
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

    @Override
    public Mono<ToDoResource> getToDoById(String toDoId)
    {
        return
                toDoAccountingQueryUseCases
                        .getToDoById(new GetToDoByIdQuery(toDoId))
                        .map(GetToDoByIdResult::getToDo)
                        .flatMap(this::toResource);
    }

    @Override
    public Mono<ToDoFullInfoResource> getToDoFullInfoById(String toDoId)
    {
        return
                toDoAccountingQueryUseCases
                        .getToDoFullInfoById(new GetToDoFullInfoByIdQuery(toDoId))
                        .map(GetToDoFullInfoByIdResult::getToDoFullInfo)
                        .flatMap(this::toToDoFullInfoResource);
    }

    private Mono<ToDoFullInfoResource> toToDoFullInfoResource(ToDoFullInfoDto toDoFullInfoDto)
    {
        return Mono.fromCallable(() -> toDoFullInfoResourceAssembler.toModel(toDoFullInfoDto));
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

    @Override
    public Mono<Void> removeToDo(String toDoId)
    {
        return
                toDoAccountingCommandUseCases
                        .removeToDo(new RemoveToDoCommand(toDoId))
                        .onErrorComplete(ToDoNotFoundException.class)
                        .then();
    }

    private Mono<ToDoResource> toResource(ToDoDto toDoDto)
    {
        return Mono.fromCallable(() -> toDoResourceAssembler.toModel(toDoDto));
    }
}
