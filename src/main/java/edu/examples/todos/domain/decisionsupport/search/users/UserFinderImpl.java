package edu.examples.todos.domain.decisionsupport.search.users;

import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.persistence.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserFinderImpl implements UserFinder
{
    private final UserRepository userRepository;

    @Override
    public Mono<User> findUserByIdAsync(UserId userId)
    {
        return Mono.fromCallable(() -> userRepository.findById(userId).orElse(null));
    }
}
