package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;

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

    public static UpdateToDoCommand createSimpleCommandForToDoUpdating(String toDoId)
    {
        return new UpdateToDoCommand(toDoId, "To-Do#" + toDoId, "To-Do#" + toDoId);
    }

    public static UpdateToDoCommand createSimpleIncorrectCommandForToDoUpdating()
    {
            return new UpdateToDoCommand();
    }

    public static UpdateToDoCommand createSimpleIncorrectCommandForToDoUpdating(String toDoId)
    {
        return new UpdateToDoCommand(toDoId, "", "");
    }

    public static RemoveToDoCommand createCommandForToDoRemoving(String toDoId)
    {
        return new RemoveToDoCommand(toDoId);
    }

    public static AssignToDoParentCommand createCommandForToDoParentAssigning(String targetToDoId, String parentToDoId)
    {
        return new AssignToDoParentCommand(targetToDoId, parentToDoId);
    }
}
