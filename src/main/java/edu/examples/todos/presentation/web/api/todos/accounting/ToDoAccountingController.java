package edu.examples.todos.presentation.web.api.todos.accounting;

import edu.examples.todos.usecases.todos.accounting.ToDoAccountingUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.dtos.ToDoDto;
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
    private final ToDoAccountingUseCases toDoAccountingUseCases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ToDoDto> createToDo(@RequestBody CreateToDoCommand createToDoCommand)
    {
        return toDoAccountingUseCases.createToDo(createToDoCommand).map(CreateToDoResult::getToDo);
    }
}
