package edu.examples.todos.presentation.api.todos.workcycle.performing;

import edu.examples.todos.presentation.api.todos.common.resources.ToDoResource;
import reactor.core.publisher.Mono;

public interface ApiToDoPerformingController
{
    Mono<ToDoResource> performToDo(String toDoId);
}
