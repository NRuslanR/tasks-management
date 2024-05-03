package edu.examples.todos.domain.operations.availability.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoActionsAvailability;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class StandardToDoActionsService implements ToDoActionsService
{
    @Override
    public Mono<ToDoActionsAvailability> getToDoActionsAvailabilityAsync(ToDo toDo)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(toDo))
                    .map(v ->
                        ToDoActionsAvailability.of(
                            true,
                            !v.isPerformed(),
                            true,
                            !v.isPerformed(),
                            !v.isPerformed()
                        )
                );
    }
}
