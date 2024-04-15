package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;

public class ToDoPerformingCommandUseCasesTestsUtils {
    public static PerformToDoCommand createCommandForToDoPerforming(String toDoId)
    {
        return new PerformToDoCommand(toDoId);
    }

    public static PerformToDoCommand createCommandForNonExistentToDoPerforming()
    {
        return new PerformToDoCommand(ToDoInfoGeneratingUtils.generateRandomToDoId());
    }
}
