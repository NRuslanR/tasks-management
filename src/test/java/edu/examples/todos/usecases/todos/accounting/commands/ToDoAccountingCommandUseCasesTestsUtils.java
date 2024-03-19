package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;

import java.util.List;
import java.util.stream.Stream;

public class ToDoAccountingCommandUseCasesTestsUtils
{
    public static List<CreateToDoCommand> createSimpleCommandsForToDoCreating(String... toDoNames)
    {
        return Stream.of(toDoNames).map(ToDoAccountingCommandUseCasesTestsUtils::createSimpleCommandForToDoCreating).toList();
    }

    public static CreateToDoCommand createSimpleCommandForToDoCreating(String toDoName)
    {
        return new CreateToDoCommand(toDoName, "");
    }

    public static CreateToDoCommand createSimpleIncorrectCommandForToDoCreating()
    {
        return new CreateToDoCommand();
    }
}
