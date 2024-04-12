package edu.examples.todos.usecases.users.accounting.queries;

import edu.examples.todos.usecases.users.accounting.common.exceptions.UserNotFoundException;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdQuery;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdResult;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.IncorrectGetUserByIdQueryException;
import reactor.core.publisher.Mono;

public interface UserAccountingQueryUseCases
{
    /**
     *
     * @param query
     * @return result
     * @throws NullPointerException
     * @throws IncorrectGetUserByIdQueryException
     * @throws UserNotFoundException
     */
    Mono<GetUserByIdResult> getUserById(GetUserByIdQuery query);
}
