package edu.examples.todos.presentation.web.api.todos.accounting;

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

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/todos")
@CrossOrigin
public class ToDoAccountingController
{
    private final ToDoAccountingQueryUseCases toDoAccountingQueryUseCases;
    private final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    @GetMapping(path = "/{toDoId}")
    public Mono<ToDoDto> getToDoById(@PathVariable("toDoId") String toDoId)
    {
        return toDoAccountingQueryUseCases.getToDoById(new GetByIdQuery(toDoId)).map(GetByIdResult::getToDo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ToDoDto> createToDo(@RequestBody CreateToDoCommand createToDoCommand)
    {
        return toDoAccountingCommandUseCases.createToDo(createToDoCommand).map(CreateToDoResult::getToDo);
    }
}
