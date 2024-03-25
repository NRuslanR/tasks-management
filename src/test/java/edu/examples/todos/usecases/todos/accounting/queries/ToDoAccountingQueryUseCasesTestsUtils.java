package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.queries.common.FindObjectsQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;

public class ToDoAccountingQueryUseCasesTestsUtils
{
    public static GetByIdQuery createGetByIdQuery(String toDoId)
    {
        return new GetByIdQuery(toDoId);
    }

    public static GetByIdQuery createIncorrectGetByIdQuery()
    {
        return new GetByIdQuery();
    }

    public static FindToDosQuery createQueryToFindAllToDos()
    {
        return new FindToDosQuery(FindObjectsQuery.allObjects());
    }
}
