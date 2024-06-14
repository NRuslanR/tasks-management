package edu.examples.todos.presentation.api.features.todos.accounting.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.examples.todos.presentation.api.common.config.ApiPaginationConfiguration;
import edu.examples.todos.presentation.api.features.todos.accounting.AbstractApiToDoAccountingController;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoFullInfoResource;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoFullInfoResourceAssembler;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.queries.ToDoAccountingQueryUseCases;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/todos")
@CrossOrigin
public class HttpApiToDoAccountingController extends AbstractApiToDoAccountingController
{
    public HttpApiToDoAccountingController(
            ToDoAccountingQueryUseCases toDoAccountingQueryUseCases,
            ToDoAccountingCommandUseCases toDoAccountingCommandUseCases,
            ToDoResourceAssembler toDoResourceAssembler,
            ToDoFullInfoResourceAssembler toDoFullInfoResourceAssembler,
            PagedResourcesAssembler<ToDoDto> pagedToDoResourceAssembler,
            ApiPaginationConfiguration paginationConfig)
    {
        super(
                toDoAccountingQueryUseCases,
                toDoAccountingCommandUseCases,
                toDoResourceAssembler,
                toDoFullInfoResourceAssembler,
                pagedToDoResourceAssembler,
                paginationConfig
        );
    }

    @Override
    @GetMapping
    public Mono<PagedModel<ToDoResource>> findToDos(
            @RequestParam(value = "page") Optional<Integer> page,
            @RequestParam(value = "size") Optional<Integer> size,
            @RequestParam(value = "sort") Optional<List<String>> sort
    )
    {
        return super.findToDos(page, size, sort);
    }

    @Override
    @GetMapping(path = "/{toDoId}")
    public Mono<ToDoResource> getToDoById(@PathVariable("toDoId") String toDoId)
    {
        return super.getToDoById(toDoId);
    }

    @Override
    @GetMapping(path = "/{toDoId}/full-info")
    public Mono<ToDoFullInfoResource> getToDoFullInfoById(@PathVariable("toDoId") String toDoId)
    {
        return super.getToDoFullInfoById(toDoId);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ToDoResource> createToDo(@RequestBody CreateToDoCommand createToDoCommand)
    {
        return super.createToDo(createToDoCommand);
    }

    @Override
    @PutMapping(path = "/{toDoId}")
    public Mono<ToDoResource> updateToDo(
            @PathVariable("toDoId") String toDoId,
            @RequestBody UpdateToDoCommand updateToDoCommand
    )
    {
        return super.updateToDo(toDoId, updateToDoCommand);
    }

    @Override
    @DeleteMapping(path = "/{toDoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeToDo(@PathVariable("toDoId") String toDoId)
    {
        return super.removeToDo(toDoId);
    }
}
