package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.common.config.UnitUseCasesTest;
import org.springframework.beans.factory.annotation.Autowired;

@UnitUseCasesTest
public class UnitToDoAccountingCommandUseCasesTests extends ToDoAccountingCommandUseCasesTests
{
    @Autowired
    public UnitToDoAccountingCommandUseCasesTests(ToDoAccountingCommandUseCases toDoAccountingUseCases) {
        super(toDoAccountingUseCases);
    }
}
