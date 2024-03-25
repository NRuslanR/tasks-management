package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.queries.common.FilterQuery;
import edu.examples.todos.usecases.todos.accounting.queries.common.FindObjectsQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

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

    public static FindToDosQuery createQueryToFindToDoPage(int pageSize)
    {
        return createQueryToFindToDoPage(0, pageSize);
    }

    public static FindToDosQuery createQueryToFindToDoPage(int page, int pageSize)
    {
        return new FindToDosQuery(FindObjectsQuery.ofPage(PageRequest.of(page, pageSize)));
    }

    public static FindToDosQuery createFindToDosQueryWithIncorrectFilter()
    {
        return new FindToDosQuery(FindObjectsQuery.filteredObjects(new FilterQuery(Map.of("", ""))));
    }
}
