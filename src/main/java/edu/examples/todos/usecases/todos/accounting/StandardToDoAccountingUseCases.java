package edu.examples.todos.usecases.todos.accounting;

import edu.examples.todos.domain.operations.creation.todos.CreateToDoReply;
import edu.examples.todos.domain.operations.creation.todos.ToDoCreationService;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommandResultMapper;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
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
    public Mono<CreateToDoResult> createToDo(CreateToDoCommand command) throws NullPointerException
    {
        var createToDoRequest = createToDoCommandResultMapper.toCreateToDoRequest(command);

        var createToDoReply = toDoCreationService.createToDo(createToDoRequest);

        return Mono
                .fromCallable(() -> processCreateToDoReply(createToDoReply))
                .flatMap(this::toCommandResultAsync)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private CreateToDoReply processCreateToDoReply(CreateToDoReply reply)
    {
        return new CreateToDoReply(toDoRepository.save(reply.getToDo()));
    }

    private Mono<CreateToDoResult> toCommandResultAsync(CreateToDoReply reply)
    {
        return Mono.just(createToDoCommandResultMapper.toCreateToDoResult(reply));
    }
}
