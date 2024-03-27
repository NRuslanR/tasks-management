package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosResult;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.IncorrectFindToDosQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.IncorrectGetToDoByIdQueryException;
import reactor.core.publisher.Mono;

/* refactor: to turn "throws" to corresponding comment because the exceptions will be wrapped by Mono as well */
public interface ToDoAccountingQueryUseCases
{
    Mono<GetToDoByIdResult> getToDoById(GetToDoByIdQuery query)
            throws NullPointerException, IncorrectGetToDoByIdQueryException, ToDoNotFoundException;

    Mono<FindToDosResult> findToDos(FindToDosQuery query)
            throws NullPointerException, IncorrectFindToDosQueryException;
}
