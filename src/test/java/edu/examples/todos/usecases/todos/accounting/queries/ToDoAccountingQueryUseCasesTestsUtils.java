package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;

public class ToDoAccountingQueryUseCasesTestsUtils
{
    public static GetByIdQuery createFindByIdQuery(String toDoId)
    {
        return new GetByIdQuery(toDoId);
    }
}
