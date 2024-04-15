package edu.examples.todos.presentation.api.todos.workcycle.performing;

import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.workcycle.performing.ToDoPerformingCommandUseCases;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoResult;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbstractApiToDoPerformingController implements ApiToDoPerformingController
{
    private final ToDoPerformingCommandUseCases toDoPerformingCommandUseCases;

    private final ToDoResourceAssembler toDoResourceAssembler;

    @Override
    public Mono<ToDoResource> performToDo(String toDoId)
    {
        return
                toDoPerformingCommandUseCases
                        .performToDo(new PerformToDoCommand(toDoId))
                        .map(PerformToDoResult::getToDo)
                        .flatMap(this::toToDoResource);
    }

    private Mono<ToDoResource> toToDoResource(ToDoDto toDoDto)
    {
        return Mono.fromCallable(() -> toDoResourceAssembler.toModel(toDoDto));
    }
}
