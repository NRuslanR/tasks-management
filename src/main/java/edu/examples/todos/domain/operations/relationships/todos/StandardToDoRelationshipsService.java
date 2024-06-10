package edu.examples.todos.domain.operations.relationships.todos;

import edu.examples.todos.domain.actors.todos.*;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import edu.examples.todos.domain.operations.accounting.todos.ToDoAccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoRelationshipsService implements ToDoRelationshipsService
{
    private final ToDoAccountingService toDoAccountingService;
    private final ToDoFinder toDoFinder;

    @Override
    public Mono<OperableToDo> assignToDoParentAsync(ToDoId targetToDoId, ToDoId parentToDoId)
            throws NullPointerException, DescendentToDoCanNotBeParentForAncestorToDoException
    {
        return
                Mono.fromCallable(() -> {

                    Objects.requireNonNull(targetToDoId);
                    Objects.requireNonNull(parentToDoId);

                    return targetToDoId;
                })
                .flatMap(toDoAccountingService::getToDoByIdForParentAssigning)
                .zipWith(toDoAccountingService.getToDoById(parentToDoId))
                .flatMap(v -> doAssignToDoParentAsync(
                        Objects.equals(v.getT1().getId(), targetToDoId) ?
                                v.getT1() : v.getT2(),

                        Objects.equals(v.getT1().getId(), parentToDoId) ?
                                v.getT1() : v.getT2())
                );
    }

    private Mono<OperableToDo> doAssignToDoParentAsync(
            OperableToDo targetToDo, OperableToDo parentToDo
    )
    {
        throwIfTargetAndParentToDosSame(targetToDo, parentToDo);
        throwIfTargetAndParentToDosAlreadyPerformed(targetToDo, parentToDo);

        return
                toDoFinder
                        .findAllSubToDosRecursivelyForAsync(targetToDo.getTarget())
                        .map(v -> {

                            throwIfListIncludesToDo(v, parentToDo.getTarget());

                            return targetToDo;
                        })
                        .map(v -> {

                            v.setParentToDoId(parentToDo.getTarget().getId());

                            return v;
                        });
    }

    private void throwIfTargetAndParentToDosSame(OperableToDo targetToDo, OperableToDo parentToDo)
    {
        if (targetToDo.getTarget().equals(parentToDo.getTarget()))
        {
            throw new DescendentToDoCanNotBeParentForAncestorToDoException(
                    "To-Do can't be parent for itself"
            );
        }
    }

    private void throwIfTargetAndParentToDosAlreadyPerformed(OperableToDo targetToDo, OperableToDo parentToDo)
    {
        if (targetToDo.isPerformed() || parentToDo.isPerformed())
        {
            throw new ToDoStateIsNotCorrectDomainException(
                    "The performed target or parent To-Dos or both " +
                    "can't be participants to parent assigning"
            );
        }
    }

    private void throwIfListIncludesToDo(ToDoList toDoList, ToDo parentToDo)
    {
        if (toDoList.contains(parentToDo))
        {
            throw new DescendentToDoCanNotBeParentForAncestorToDoException();
        }
    }
}
