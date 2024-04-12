package edu.examples.todos.usecases.users.accounting.queries;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCases;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationUserAccountingQueryUseCasesTests extends UserAccountingQueryUseCasesTests
{
    @Autowired
    public IntegrationUserAccountingQueryUseCasesTests(
            UserAccountingQueryUseCases userAccountingQueryUseCases,
            UserAccountingCommandUseCases userAccountingCommandUseCases
    )
    {
        super(userAccountingQueryUseCases, userAccountingCommandUseCases);
    }
}
