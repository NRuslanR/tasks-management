package edu.examples.todos.domain.operations.availability.todos;

import edu.examples.todos.domain.actors.todos.ToDoActionsAvailability;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.resources.users.UserId;
import reactor.core.publisher.Mono;

public interface UserToDoActionsService
{
    Mono<ToDoActionsAvailability> getToDoActionsAvailabilityForUserAsync(ToDoId toDoId, UserId userId);

    Mono<ToDoActionsAvailability> ensureToDoViewingAvailableForUserAsync(ToDoId toDoId, UserId userId);

    Mono<ToDoActionsAvailability> ensureAnyToDoActionAvailableForUserAsync(ToDoId toDoId, UserId userId);

    Mono<ToDoActionsAvailability> ensureToDoChangingAvailableForUserAsync(ToDoId toDoId, UserId userId);

    Mono<ToDoActionsAvailability> ensureToDoRemovingAvailableForUserAsync(ToDoId toDoId, UserId userId);

    Mono<ToDoActionsAvailability> ensureToDoParentAssigningAvailableForUserAsync(ToDoId toDoId, UserId userId);

    Mono<ToDoActionsAvailability> ensureToDoPerformingAvailableForUserAsync(ToDoId toDoId, UserId userId);

    Mono<UserId> ensureUserCanCreateToDos(UserId userId);
}
