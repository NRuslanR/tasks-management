package edu.examples.todos.presentation.api.features.todos.workcycle.performing;

import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResource;
import reactor.core.publisher.Mono;

public interface ApiToDoPerformingController
{
    Mono<ToDoResource> performToDo(String toDoId);
}
