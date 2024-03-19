package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.IncorrectGetByIdQueryException;
import reactor.core.publisher.Mono;

public interface ToDoAccountingQueryUseCases
{
    Mono<GetByIdResult> getToDoById(GetByIdQuery query)
            throws NullPointerException, IncorrectGetByIdQueryException, ToDoNotFoundException;
}
