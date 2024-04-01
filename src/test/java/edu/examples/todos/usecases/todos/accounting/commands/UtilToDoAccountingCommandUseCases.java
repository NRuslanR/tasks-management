package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import org.javatuples.KeyValue;
import reactor.core.publisher.Mono;

import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoName;

public interface UtilToDoAccountingCommandUseCases
{
    default KeyValue<CreateToDoCommand, Mono<CreateToDoResult>> runCreateRandomToDoCommandFor()
    {
        return runCreateToDoCommandFor(generateRandomToDoName());
    }

    KeyValue<CreateToDoCommand, Mono<CreateToDoResult>> runCreateToDoCommandFor(String toDoName);
}
