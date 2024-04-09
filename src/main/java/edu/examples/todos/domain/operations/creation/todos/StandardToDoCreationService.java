package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoException;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.actors.todos.ToDoPriority;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import lombok.NonNull;
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

    @Override
    public CreateToDoReply createToDo(@NonNull CreateToDoRequest request)
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
                        .flatMap(t -> Mono.just(CreateToDoReply.of(t)));
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
                        .then(createToDoFromRequest(request))
                        .onErrorResume(
                                ToDoException.class,
                                e -> Mono.error(new IncorrectCreateToDoRequestException(e.getMessage()))
                        );
    }

    private Mono<ToDo> createToDoFromRequest(CreateToDoRequest request)
    {
        var toDoId = new ToDoId(UUID.randomUUID());

        var createdAt = LocalDateTime.now();

        return Mono.fromCallable(
                () -> new ToDo(
                        toDoId,
                        request.getName(),
                        request.getDescription(),
                        request.getPriority().orElseGet(ToDoPriority::defaultPriority),
                        createdAt
                )
        );
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
}
