package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationToDoAccountingCommandUseCasesTests extends ToDoAccountingCommandUseCasesTests
{
    @Autowired
    public IntegrationToDoAccountingCommandUseCasesTests(ToDoAccountingCommandUseCases toDoAccountingUseCases)
    {
        super(toDoAccountingUseCases);
    }
}
