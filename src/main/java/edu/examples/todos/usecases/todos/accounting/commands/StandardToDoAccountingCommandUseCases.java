package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.creation.todos.*;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.todos.accounting.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommandResultMapper;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoAccountingCommandUseCases implements ToDoAccountingCommandUseCases
{
    private final ToDoCreationService toDoCreationService;

    private final CreateToDoCommandResultMapper createToDoCommandResultMapper;

    private final UpdateToDoCommandResultMapper updateToDoCommandResultMapper;

    private final ToDoRepository toDoRepository;

    @Override
    @Transactional
    public Mono<CreateToDoResult> createToDo(CreateToDoCommand command)
            throws NullPointerException, IncorrectCreateToDoCommandException, ToDoAlreadyExistsException
    {
        return ensureCreateToDoCommandIsValid(command)
                .map(createToDoCommandResultMapper::toCreateToDoRequest)
                .flatMap(this::doCreateToDo)
                .flatMap(this::processCreateToDoReply)
                .flatMap(this::toCreateToDoCommandResultAsync)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<CreateToDoCommand> ensureCreateToDoCommandIsValid(CreateToDoCommand command)
    {
        return Mono.fromCallable(() -> Objects.requireNonNull(command));
    }

    private Mono<CreateToDoReply> doCreateToDo(CreateToDoRequest createToDoRequest)
    {
        return
                toDoCreationService.createToDoAsync(createToDoRequest)
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
        return
                saveToDo(reply.getToDo())
                    .map(CreateToDoReply::new);
    }

    private Mono<CreateToDoResult> toCreateToDoCommandResultAsync(CreateToDoReply reply)
    {
        return Mono.just(createToDoCommandResultMapper.toCreateToDoResult(reply));
    }

    @Override
    public Mono<UpdateToDoResult> updateToDo(
            UpdateToDoCommand updateToDoCommand
    )
    {
        return
                ensureUpdateToDoCommandIsValid(updateToDoCommand)
                        .flatMap(this::doUpdateToDo);
    }

    private Mono<UpdateToDoCommand> ensureUpdateToDoCommandIsValid(UpdateToDoCommand updateToDoCommand)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(updateToDoCommand))
                        .map(v -> {

                            if (StringUtils.hasText(v.getToDoId()))
                                return v;

                            throw new IncorrectUpdateToDoCommandException("Incorrect To-Do id");
                        });
    }

    private Mono<UpdateToDoResult> doUpdateToDo(UpdateToDoCommand updateToDoCommand)
    {
        return
                getToDoById(updateToDoCommand.getToDoId())
                        .map(v -> updateToDoCommandResultMapper.changeToDoByCommand(v, updateToDoCommand))
                        .flatMap(this::saveToDo)
                        .map(v -> updateToDoCommandResultMapper.toUpdateToDoResult(v));
    }

    private Mono<ToDo> getToDoById(String toDoId)
    {
        return
                Mono
                    .fromCallable(
                        () ->
                            toDoRepository
                                .findById(ToDoId.of(toDoId))
                                    .orElseThrow(ToDoNotFoundException::new)
                    );
    }

    private Mono<ToDo> saveToDo(ToDo toDo)
    {
        return
                Mono
                    .fromCallable(() -> toDoRepository.save(toDo))
                    .onErrorResume(
                            OptimisticLockingFailureException.class,
                            v -> Mono.error(
                                    new SavingToDoException(
                                        "Attempt to simultaneously save To-Do against to other clients"
                                    )
                            )
                    );
    }
}
