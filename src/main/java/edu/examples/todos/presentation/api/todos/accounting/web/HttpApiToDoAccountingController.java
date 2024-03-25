package edu.examples.todos.presentation.api.todos.accounting.web;

import edu.examples.todos.presentation.api.common.config.ApiPaginationConfiguration;
import edu.examples.todos.presentation.api.todos.accounting.common.AbstractApiToDoAccountingController;
import edu.examples.todos.presentation.api.todos.accounting.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.accounting.common.resources.ToDoResourceAssembler;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.queries.ToDoAccountingQueryUseCases;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/todos")
@CrossOrigin
public class HttpApiToDoAccountingController extends AbstractApiToDoAccountingController
{
    public HttpApiToDoAccountingController(
            ToDoAccountingQueryUseCases toDoAccountingQueryUseCases,
            ToDoAccountingCommandUseCases toDoAccountingCommandUseCases,
            ToDoResourceAssembler toDoResourceAssembler,
            PagedResourcesAssembler<ToDoDto> pagedToDoResourcesAssembler,
            ApiPaginationConfiguration paginationConfig
    )
    {
        super(toDoAccountingQueryUseCases, toDoAccountingCommandUseCases, toDoResourceAssembler, pagedToDoResourcesAssembler, paginationConfig);
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ToDoResource> createToDo(@RequestBody CreateToDoCommand createToDoCommand)
    {
        return super.createToDo(createToDoCommand);
    }
}
