package edu.examples.todos.usecases.users.accounting.commands;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationUserAccountingCommandUseCasesTests extends UserAccountingCommandUseCasesTests
{
    @Autowired
    public IntegrationUserAccountingCommandUseCasesTests(UserAccountingCommandUseCases userAccountingCommandUseCases)
    {
        super(userAccountingCommandUseCases);
    }
}
