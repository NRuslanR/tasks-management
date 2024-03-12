package edu.examples.todos.usecases.todos.accounting;

import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import reactor.core.publisher.Mono;

public interface ToDoAccountingUseCases
{
    Mono<CreateToDoResult> createToDo(CreateToDoCommand command) throws NullPointerException;
}
