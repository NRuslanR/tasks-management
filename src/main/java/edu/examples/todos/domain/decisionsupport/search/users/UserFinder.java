package edu.examples.todos.domain.decisionsupport.search.users;

import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import reactor.core.publisher.Mono;

public interface UserFinder
{
    Mono<User> findUserByIdAsync(UserId userId);
}
