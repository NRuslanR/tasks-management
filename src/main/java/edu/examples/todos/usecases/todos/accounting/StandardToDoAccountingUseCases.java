package edu.examples.todos.usecases.todos.accounting;

import edu.examples.todos.domain.operations.creation.todos.*;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommandResultMapper;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class StandardToDoAccountingUseCases implements ToDoAccountingUseCases
{
    private final ToDoCreationService toDoCreationService;

    private final CreateToDoCommandResultMapper createToDoCommandResultMapper;

    private final ToDoRepository toDoRepository;

    @Override
    @Transactional
    public Mono<CreateToDoResult> createToDo(@NonNull CreateToDoCommand command)
            throws NullPointerException, IncorrectCreateToDoCommandException, ToDoAlreadyExistsException
    {
        return Mono
                .fromCallable(() -> createToDoCommandResultMapper.toCreateToDoRequest(command))
                .flatMap(this::doCreateToDo)
                .flatMap(this::processCreateToDoReply)
                .flatMap(this::toCommandResultAsync)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<CreateToDoReply> doCreateToDo(CreateToDoRequest createToDoRequest)
            throws ToDoAlreadyExistsException, IncorrectCreateToDoCommandException
    {
        return
            Mono
                .fromCallable(() -> toDoCreationService.createToDo(createToDoRequest))
                    .onErrorResume(
                            ToDoAlreadyExistsDomainException.class,
                            e -> Mono.error(new ToDoAlreadyExistsException(e.getMessage()))
                    )
                    .onErrorResume(
                            IncorrectCreateToDoRequestException.class,
                            e -> Mono.error(new IncorrectCreateToDoCommandException(e.getMessage()))
                    );
    }

    private Mono<CreateToDoReply> processCreateToDoReply(CreateToDoReply reply)
    {
        return Mono.fromCallable(() -> new CreateToDoReply(toDoRepository.save(reply.getToDo())));
    }

    private Mono<CreateToDoResult> toCommandResultAsync(CreateToDoReply reply)
    {
        return Mono.just(createToDoCommandResultMapper.toCreateToDoResult(reply));
    }
}
