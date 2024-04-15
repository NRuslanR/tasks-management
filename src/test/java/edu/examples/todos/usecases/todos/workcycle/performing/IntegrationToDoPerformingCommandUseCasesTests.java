package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationToDoPerformingCommandUseCasesTests extends ToDoPerformingCommandUseCasesTests
{
    @Autowired
    public IntegrationToDoPerformingCommandUseCasesTests(
            ToDoCreationUtilService toDoCreationUtilService,
            ToDoPerformingCommandUseCases toDoPerformingCommandUseCases
    ) {
        super(toDoCreationUtilService, toDoPerformingCommandUseCases);
    }
}
