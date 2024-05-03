package edu.examples.todos.domain.operations.availability.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoActionsAvailability;
import reactor.core.publisher.Mono;

public interface ToDoActionsService
{
    Mono<ToDoActionsAvailability> getToDoActionsAvailabilityAsync(ToDo toDo);
}
