package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.IncorrectGetByIdQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosResult;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.IncorrectFindToDosQueryException;
import reactor.core.publisher.Mono;

public interface ToDoAccountingQueryUseCases
{
    Mono<GetByIdResult> getToDoById(GetByIdQuery query)
            throws NullPointerException, IncorrectGetByIdQueryException, ToDoNotFoundException;

    Mono<FindToDosResult> findToDos(FindToDosQuery query)
            throws NullPointerException, IncorrectFindToDosQueryException;
}
