package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.IncorrectPerformToDoCommandException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoResult;
import reactor.core.publisher.Mono;

public interface ToDoPerformingCommandUseCases
{
    /**
     * @param command
     * @return result
     * @throws NullPointerException
     * @throws IncorrectPerformToDoCommandException
     * @throws ToDoNotFoundException
     * @throws ToDoStatusIsNotCorrectException
     */
    Mono<PerformToDoResult> performToDo(PerformToDoCommand command);
}
