package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.*;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import edu.examples.todos.domain.decisionsupport.search.users.UserFinder;
import edu.examples.todos.domain.operations.availability.todos.ToDoActionsService;
import edu.examples.todos.domain.operations.creation.users.UserNotFoundDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StandardToDoCreationService implements ToDoCreationService
{
    private final ToDoFinder toDoFinder;
    private final UserFinder userFinder;

    private final ToDoActionsService toDoActionsService;

    @Override
    public CreateToDoReply createToDo(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException
    {
        return createToDoAsync(request).block();
    }

    @Override
    public Mono<CreateToDoReply> createToDoAsync(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException
    {
        return
                ensureRequestIsValid(request)
                        .flatMap(this::doCreateToDo)
                        .flatMap(this::toOperableToDo)
                        .map(CreateToDoReply::of);
    }

    private Mono<CreateToDoRequest> ensureRequestIsValid(CreateToDoRequest request)
    {
        return Mono.fromCallable(() -> {

            Objects.requireNonNull(request);

            return request;

        });
    }

    private Mono<ToDo> doCreateToDo(CreateToDoRequest request)
    {
        return
                ensureToDoWithSpecifiedNameDoesNotExists(request.getName())
                    .then(Mono.defer(() -> createToDoFromRequest(request)))
                    .onErrorResume(
                            ToDoException.class,
                            e -> Mono.error(new IncorrectCreateToDoRequestException(e.getMessage()))
                    );
    }

    private Mono<ToDo> createToDoFromRequest(CreateToDoRequest request)
    {
        return
                userFinder
                        .findUserByIdAsync(request.getAuthorId())
                        .switchIfEmpty(Mono.error(new UserNotFoundDomainException("To-Do's author not found to be created")))
                        .map(author -> {

                            var toDoId = ToDoId.of(UUID.randomUUID());

                            var createdAt = LocalDateTime.now();

                            return new ToDo(
                                toDoId,
                                request.getName(),
                                request.getDescription(),
                                request.getPriority().orElseGet(ToDoPriority::defaultPriority),
                                createdAt,
                                author
                            );
                        });
    }

    private Mono<Void> ensureToDoWithSpecifiedNameDoesNotExists(String name)
    {
        return
                toDoFinder
                    .findToDoByNameAsync(name)
                    .doOnSuccess(t -> {

                        if (!Objects.isNull(t))
                        {
                            throw new ToDoAlreadyExistsDomainException(name);
                        }
                    })
                    .then();
    }

    private Mono<OperableToDo> toOperableToDo(ToDo toDo)
    {
        return
                toDoActionsService
                    .getToDoActionsAvailabilityAsync(toDo)
                    .map(v -> OperableToDo.of(toDo, v));
    }
}
