package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationToDoAccountingQueryUseCasesTests extends ToDoAccountingQueryUseCasesTests
{
    @Autowired
    public IntegrationToDoAccountingQueryUseCasesTests(ToDoAccountingQueryUseCases toDoAccountingQueryUseCases)
    {
        super(toDoAccountingQueryUseCases);
    }
}
