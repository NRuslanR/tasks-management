package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoStateIsNotCorrectException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.IncorrectPerformToDoCommandException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoResult;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface ToDoPerformingCommandUseCases
{
    /**
     * @param command
     * @return result
     * @throws NullPointerException
     * @throws IncorrectPerformToDoCommandException
     * @throws ToDoNotFoundException
     * @throws ToDoStateIsNotCorrectException
     */
    Mono<PerformToDoResult> performToDo(@Valid PerformToDoCommand command);
}
