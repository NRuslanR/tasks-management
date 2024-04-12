package edu.examples.todos.usecases.users.accounting.queries;

import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdQuery;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserAccountingQueryUseCasesTestsUtils
{
    public static Collection<GetUserByIdQuery> createIncorrectQueriesForGettingUserById()
    {
        return List.of(
                new GetUserByIdQuery(),
                new GetUserByIdQuery("  ")
        );
    }

    public static GetUserByIdQuery createQueryForUserNotFoundById()
    {
        return new GetUserByIdQuery(UUID.randomUUID().toString());
    }
}
