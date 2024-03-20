package edu.examples.todos.presentation.web.api.todos.accounting;

import edu.examples.todos.presentation.web.api.todos.accounting.resources.ToDoResource;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.queries.ToDoAccountingQueryUseCases;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/todos")
@CrossOrigin
public class ToDoAccountingController
{
    private final ToDoAccountingQueryUseCases toDoAccountingQueryUseCases;
    private final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    @GetMapping(path = "/{toDoId}")
    public Mono<ToDoResource> getToDoById(@PathVariable("toDoId") String toDoId)
    {
        return
                toDoAccountingQueryUseCases
                    .getToDoById(new GetByIdQuery(toDoId))
                        .flatMap(this::toToDoResource);
    }

    private Mono<ToDoResource> toToDoResource(GetByIdResult result)
    {
        return toToDoResource(result.getToDo());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ToDoResource> createToDo(@RequestBody CreateToDoCommand createToDoCommand)
    {
        return
                toDoAccountingCommandUseCases
                    .createToDo(createToDoCommand)
                    .flatMap(this::toToDoResource);
    }

    private Mono<ToDoResource> toToDoResource(CreateToDoResult result)
    {
        return toToDoResource(result.getToDo());

    }

    private Mono<ToDoResource> toToDoResource(ToDoDto toDoDto)
    {
        return
                linkTo(
                    methodOn(ToDoAccountingController.class).getToDoById(toDoDto.getId())
                )
                .withSelfRel()
                .toMono().map(l -> ToDoResource.of(toDoDto, l));
    }
}
