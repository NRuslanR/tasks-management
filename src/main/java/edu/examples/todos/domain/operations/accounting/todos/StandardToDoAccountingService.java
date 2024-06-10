package edu.examples.todos.domain.operations.accounting.todos;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import edu.examples.todos.domain.operations.availability.todos.ToDoActionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class StandardToDoAccountingService implements ToDoAccountingService
{
    private final ToDoFinder toDoFinder;
    private final ToDoActionsService toDoActionsService;

    @Override
    public Mono<OperableToDo> getToDoById(ToDoId toDoId) throws ToDoNotFoundDomainException
    {
        return getToDoByIdFor(toDoId, this::ensureToDoViewingAvailable);
    }

    @Override
    public Mono<OperableToDo> getToDoByIdForChanging(ToDoId toDoId) throws ToDoNotFoundDomainException
    {
        return getToDoByIdFor(toDoId, this::ensureToDoChangingAvailable);
    }

    @Override
    public Mono<OperableToDo> getToDoByIdForRemoving(ToDoId toDoId) throws ToDoNotFoundDomainException
    {
        return getToDoByIdFor(toDoId, this::ensureToDoRemovingAvailable);
    }

    @Override
    public Mono<OperableToDo> getToDoByIdForParentAssigning(ToDoId toDoId)
    {
        return getToDoByIdFor(toDoId, this::ensureToDoParentAssigningAvailable);
    }

    @Override
    public Mono<OperableToDo> getToDoByIdForPerforming(ToDoId toDoId)
    {
        return getToDoByIdFor(toDoId, this::ensureToDoPerformingAvailable);
    }

    private Mono<OperableToDo> getToDoByIdFor(ToDoId toDoId, UnaryOperator<OperableToDo> ensureAction)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(toDoId))
                    .flatMap(toDoFinder::findToDoByIdAsync)
                    .switchIfEmpty(Mono.error(new ToDoNotFoundDomainException()))
                    .flatMap(this::toOperableToDoAsync)
                    .map(ensureAction);
    }

    public Mono<OperableToDo> toOperableToDoAsync(ToDo toDo)
    {
        return
                toDoActionsService
                    .getToDoActionsAvailabilityAsync(toDo)
                    .map(v -> OperableToDo.of(toDo, v));
    }

    private OperableToDo ensureToDoViewingAvailable(OperableToDo operableToDo)
    {
        operableToDo.ensureViewingAvailable();

        return operableToDo;
    }

    private OperableToDo ensureToDoChangingAvailable(OperableToDo operableToDo)
    {
        operableToDo.ensureChangingAvailable();

        return operableToDo;
    }

    private OperableToDo ensureToDoRemovingAvailable(OperableToDo operableToDo)
    {
        operableToDo.ensureRemovingAvailable();

        return operableToDo;
    }

    private OperableToDo ensureToDoParentAssigningAvailable(OperableToDo operableToDo)
    {
        operableToDo.ensureParentAssigningAvailable();

        return operableToDo;
    }

    private OperableToDo ensureToDoPerformingAvailable(OperableToDo operableToDo)
    {
        operableToDo.ensurePerformingAvailable();

        return operableToDo;
    }
}
