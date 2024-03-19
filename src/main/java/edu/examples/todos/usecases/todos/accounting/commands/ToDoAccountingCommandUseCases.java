package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import reactor.core.publisher.Mono;

public interface ToDoAccountingCommandUseCases
{
    Mono<CreateToDoResult> createToDo(CreateToDoCommand command)
            throws NullPointerException, IncorrectCreateToDoCommandException, ToDoAlreadyExistsException;
}
