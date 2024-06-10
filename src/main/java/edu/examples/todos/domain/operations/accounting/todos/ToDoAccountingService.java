package edu.examples.todos.domain.operations.accounting.todos;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import reactor.core.publisher.Mono;

public interface ToDoAccountingService
{
    /**
     *
     * @param toDoId
     * @return toDo
     * @throws NullPointerException
     * @throws ToDoNotFoundDomainException
     */
    Mono<OperableToDo> getToDoById(ToDoId toDoId);

    Mono<OperableToDo> getToDoByIdForChanging(ToDoId toDoId);

    Mono<OperableToDo> getToDoByIdForRemoving(ToDoId toDoId);

    Mono<OperableToDo> getToDoByIdForParentAssigning(ToDoId targetToDoId);

    Mono<OperableToDo> getToDoByIdForPerforming(ToDoId toDoId);

    Mono<OperableToDo> toOperableToDoAsync(ToDo toDo);
}
