package edu.examples.todos.presentation.api.todos.workcycle.performing.web;

import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import edu.examples.todos.presentation.api.todos.common.resources.ToDoResourceAssembler;
import edu.examples.todos.presentation.api.todos.workcycle.performing.AbstractApiToDoPerformingController;
import edu.examples.todos.usecases.todos.workcycle.performing.ToDoPerformingCommandUseCases;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/todos")
public class HttpApiToDoPerformingController extends AbstractApiToDoPerformingController
{
    public HttpApiToDoPerformingController(
            ToDoPerformingCommandUseCases toDoPerformingCommandUseCases,
            ToDoResourceAssembler toDoResourceAssembler
    )
    {
        super(toDoPerformingCommandUseCases, toDoResourceAssembler);
    }

    @Override
    @PostMapping(path = "/{toDoId}/perform")
    public Mono<ToDoResource> performToDo(@PathVariable("toDoId") String toDoId)
    {
        return super.performToDo(toDoId);
    }
}
