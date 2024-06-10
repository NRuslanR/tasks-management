package edu.examples.todos.domain.operations.availability.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoActionIsNotAvailableException;
import edu.examples.todos.domain.actors.todos.ToDoActionsAvailability;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import edu.examples.todos.domain.decisionsupport.search.users.UserFinder;
import edu.examples.todos.domain.operations.accounting.todos.ToDoNotFoundDomainException;
import edu.examples.todos.domain.operations.creation.users.UserNotFoundDomainException;
import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
@Service
public class StandardUserToDoActionsService implements UserToDoActionsService
{
    private final ToDoFinder toDoFinder;
    private final UserFinder userFinder;

    @Override
    public Mono<ToDoActionsAvailability> ensureAnyToDoActionAvailableForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoActionsAvailabilityForUserAsync(toDoId, userId)
                        .map(this::ensureAnyToDoActionAvailableForUser);
    }

    @Override
    public Mono<ToDoActionsAvailability> ensureToDoViewingAvailableForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoActionsAvailabilityForUserAsync(toDoId, userId)
                    .map(v -> {
                        if (!v.isViewingAvailable())
                        {
                            throw new ToDoActionIsNotAvailableException("To-Do's viewing isn't available");
                        }

                        return v;
                    });
    }

    @Override
    public Mono<ToDoActionsAvailability> ensureToDoChangingAvailableForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoActionsAvailabilityForUserAsync(toDoId, userId)
                        .map(v -> {
                            if (!v.isChangingAvailable())
                            {
                                throw new ToDoActionIsNotAvailableException("To-Do's changing isn't available");
                            }

                            return v;
                        });
    }

    @Override
    public Mono<ToDoActionsAvailability> ensureToDoRemovingAvailableForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoActionsAvailabilityForUserAsync(toDoId, userId)
                        .map(v -> {
                            if (!v.isRemovingAvailable())
                            {
                                throw new ToDoActionIsNotAvailableException("To-Do's removing isn't available");
                            }

                            return v;
                        });
    }

    @Override
    public Mono<ToDoActionsAvailability> ensureToDoParentAssigningAvailableForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoActionsAvailabilityForUserAsync(toDoId, userId)
                        .map(v -> {
                            if (!v.isParentAssigningAvailable())
                            {
                                throw new ToDoActionIsNotAvailableException("To-Do's parent assigning isn't available");
                            }

                            return null;
                        });
    }

    @Override
    public Mono<ToDoActionsAvailability> ensureToDoPerformingAvailableForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoActionsAvailabilityForUserAsync(toDoId, userId)
                        .map(v -> {
                            if (!v.isPerformingAvailable())
                            {
                                throw new ToDoActionIsNotAvailableException("To-Do's performing isn't available");
                            }

                            return v;
                        });
    }

    @Override
    public Mono<UserId> ensureUserCanCreateToDos(UserId userId)
    {
        return
                getUserById(userId)
                        .zipWhen(v -> toDoFinder.getCreatedToDoCountByAuthorAsync(userId))
                        .map(v -> {
                            var user = v.getT1();
                            var createdToDoCount = v.getT2();

                            if (user.allowedToDoCreationCount() < createdToDoCount)
                            {
                                throw new ToDoActionIsNotAvailableException("User has exceeded To-Do's creation count limit");
                            }

                            return userId;
                        });
    }

    @Override
    public Mono<ToDoActionsAvailability> getToDoActionsAvailabilityForUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                getToDoAndUserAsync(toDoId, userId)
                    .map(v -> createUserToDoActionsAvailability(v.getT1(), v.getT2()));
    }

    private ToDoActionsAvailability ensureAnyToDoActionAvailableForUser(ToDoActionsAvailability toDoActionsAvailability)
    {
        if (!toDoActionsAvailability.anyActionAvailable())
        {
            throw new ToDoActionIsNotAvailableException("None of ToDo's actions is available");
        }

        return toDoActionsAvailability;
    }

    private Mono<Tuple2<ToDo, User>> getToDoAndUserAsync(ToDoId toDoId, UserId userId)
    {
        return
                Mono.zip(
                        toDoFinder
                                .findToDoByIdAsync(toDoId)
                                .switchIfEmpty(Mono.error(new ToDoNotFoundDomainException())),
                        getUserById(userId)
                );
    }

    private Mono<User> getUserById(UserId userId)
    {
        return
                userFinder
                    .findUserByIdAsync(userId)
                    .switchIfEmpty(Mono.error(new UserNotFoundDomainException()));
    }

    private ToDoActionsAvailability createUserToDoActionsAvailability(ToDo toDo, User user)
    {
        var isUserToDoAuthor = user.equals(toDo.getAuthor());

        var toDoActionsAvailability =
                ToDoActionsAvailability.of(
                        toDoViewingAvailableForUser(toDo, user, isUserToDoAuthor),
                        toDoChangingAvailableForUser(toDo, user, isUserToDoAuthor),
                        toDoRemovingAvailableForUser(toDo, user, isUserToDoAuthor),
                        toDoParentAssigningAvailableForUser(toDo, user, isUserToDoAuthor),
                        toDoPerformingAvailableForUser(toDo, user, isUserToDoAuthor)
                );

        return toDoActionsAvailability;
    }

    private boolean toDoViewingAvailableForUser(ToDo toDo, User user, boolean isUserToDoAuthor)
    {
        return
                isUserToDoAuthor ||
                user.canEditForeignTodos() ||
                user.canRemoveForeignTodos() ||
                user.canPerformForeignTodos();
    }

    private boolean toDoChangingAvailableForUser(ToDo toDo, User user, boolean isUserToDoAuthor)
    {
        return isUserToDoAuthor || user.canEditForeignTodos();
    }

    private boolean toDoRemovingAvailableForUser(ToDo toDo, User user, boolean isUserToDoAuthor)
    {
        return isUserToDoAuthor || user.canRemoveForeignTodos();
    }

    private boolean toDoParentAssigningAvailableForUser(ToDo toDo, User user, boolean isUserToDoAuthor)
    {
        return isUserToDoAuthor || user.canEditForeignTodos();
    }

    private boolean toDoPerformingAvailableForUser(ToDo toDo, User user, boolean isUserToDoAuthor)
    {
        return isUserToDoAuthor || user.canPerformForeignTodos();
    }
}
