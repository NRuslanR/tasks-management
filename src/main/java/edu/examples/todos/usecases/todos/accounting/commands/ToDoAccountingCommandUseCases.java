package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.create.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.IncorrectRemoveToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.update.IncorrectUpdateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoResult;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import reactor.core.publisher.Mono;

/* refactor: to turn "throws" to corresponding comment because the exceptions will be wrapped by Mono as well */
public interface ToDoAccountingCommandUseCases
{
    Mono<CreateToDoResult> createToDo(CreateToDoCommand command)
            throws NullPointerException, IncorrectCreateToDoCommandException, ToDoAlreadyExistsException;

    Mono<UpdateToDoResult> updateToDo(UpdateToDoCommand updateToDoCommand)
            throws
                NullPointerException,
                IncorrectUpdateToDoCommandException,
                ToDoNotFoundException;

    Mono<RemoveToDoResult> removeToDo(RemoveToDoCommand command)
        throws
            NullPointerException,
            IncorrectRemoveToDoCommandException,
            ToDoNotFoundException;
}
