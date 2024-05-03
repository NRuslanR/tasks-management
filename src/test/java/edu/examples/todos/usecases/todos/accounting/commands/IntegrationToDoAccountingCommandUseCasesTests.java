package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import edu.examples.todos.usecases.todos.common.behaviour.states.ToDoStateUtilService;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationToDoAccountingCommandUseCasesTests extends ToDoAccountingCommandUseCasesTests
{
    @Autowired
    public IntegrationToDoAccountingCommandUseCasesTests(
            ToDoAccountingCommandUseCases toDoAccountingCommandUseCases,
            UtilToDoAccountingCommandUseCases utilToDoAccountingCommandUseCases,
            ToDoCreationUtilService toDoCreationUtilService,
            ToDoStateUtilService toDoStateUtilService
    )
    {
        super(toDoAccountingCommandUseCases, utilToDoAccountingCommandUseCases, toDoCreationUtilService, toDoStateUtilService);
    }
}
