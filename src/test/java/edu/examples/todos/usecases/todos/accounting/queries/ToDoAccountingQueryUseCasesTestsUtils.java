package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.queries.common.FilterQuery;
import edu.examples.todos.usecases.todos.accounting.queries.common.FindObjectsQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdQuery;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public class ToDoAccountingQueryUseCasesTestsUtils
{
    public static GetToDoByIdQuery createGetToDoByIdQuery(String toDoId)
    {
        return new GetToDoByIdQuery(toDoId);
    }

    public static GetToDoByIdQuery createIncorrectGetToDoByIdQuery()
    {
        return new GetToDoByIdQuery();
    }

    public static GetToDoFullInfoByIdQuery createIncorrectGetToDoFullInfoByIdQuery()
    {
        return new GetToDoFullInfoByIdQuery();
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

    public static GetToDoFullInfoByIdQuery createGetToDoFullInfoByIdQuery(String toDoId)
    {
        return new GetToDoFullInfoByIdQuery(toDoId);
    }
}
