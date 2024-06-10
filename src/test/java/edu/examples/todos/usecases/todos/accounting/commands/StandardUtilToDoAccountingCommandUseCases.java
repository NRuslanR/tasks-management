package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.common.config.profiles.EnabledIfTestsProfile;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import lombok.RequiredArgsConstructor;
import org.javatuples.KeyValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component /* TestComponent doesn't work - autowired isn't triggered */
@EnabledIfTestsProfile
@RequiredArgsConstructor
public class StandardUtilToDoAccountingCommandUseCases
        implements UtilToDoAccountingCommandUseCases, ToDoCreationUtilService
{
    private final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    @Override
    public KeyValue<CreateToDoCommand, Mono<CreateToDoResult>> runCreateToDoCommandFor(String toDoName)
    {
        var command = ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoCreating(toDoName);

        var result = toDoAccountingCommandUseCases.createToDo(command);

        return KeyValue.with(command, result);
    }

    @Override
    public ToDoDto createToDo(String toDoName)
    {
        return
                runCreateToDoCommandFor(toDoName)
                        .getValue()
                        .block()
                        .getToDo();
    }
}
