package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.common.exceptions.DomainException;
import edu.examples.todos.domain.operations.creation.todos.*;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.todos.accounting.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.IncorrectRemoveToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.update.IncorrectUpdateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.SavingToDoException;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoAccountingCommandUseCases implements ToDoAccountingCommandUseCases
{
    private final ToDoCreationService toDoCreationService;

    private final UseCaseMapper mapper;

    private final ToDoRepository toDoRepository;

    @Override
    @Transactional
    public Mono<CreateToDoResult> createToDo(CreateToDoCommand command)
            throws NullPointerException, IncorrectCreateToDoCommandException, ToDoAlreadyExistsException
    {
        return ensureCreateToDoCommandIsValid(command)
                .flatMap(this::toCreateToDoRequest)
                .flatMap(this::doCreateToDo)
                .flatMap(this::processCreateToDoReply)
                .flatMap(this::toCreateToDoCommandResultAsync)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<CreateToDoCommand> ensureCreateToDoCommandIsValid(CreateToDoCommand command)
    {
        return Mono.fromCallable(() -> Objects.requireNonNull(command));
    }

    private Mono<CreateToDoRequest> toCreateToDoRequest(CreateToDoCommand createToDoCommand)
    {
        return Mono.just(mapper.map(createToDoCommand, CreateToDoRequest.class));
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
        return Mono.just(mapper.map(reply, CreateToDoResult.class));
    }

    @Override
    @Transactional
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

                            throw new IncorrectUpdateToDoCommandException("Incorrect To-Do id to update");
                        });
    }

    private Mono<UpdateToDoResult> doUpdateToDo(UpdateToDoCommand updateToDoCommand)
    {
        return
                getToDoById(updateToDoCommand.getToDoId())
                        .map(v -> mapper.map(updateToDoCommand, v))
                        .onErrorResume(
                                DomainException.class,
                                e -> Mono.error(
                                        new IncorrectUpdateToDoCommandException(e.getMessage())
                                )
                        )
                        .flatMap(this::saveToDo)
                        .map(this::toUpdateToDoResult);
    }

    private UpdateToDoResult toUpdateToDoResult(ToDo toDo)
    {
        return new UpdateToDoResult(mapper.map(toDo, ToDoDto.class));
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

    @Override
    @Transactional
    public Mono<RemoveToDoResult> removeToDo(RemoveToDoCommand command)
            throws
            NullPointerException,
            IncorrectRemoveToDoCommandException,
            ToDoNotFoundException,
            SavingToDoException
    {
        return
                ensureRemoveToDoCommandIsValid(command)
                        .flatMap(this::doRemoveToDo);
    }

    private Mono<RemoveToDoCommand> ensureRemoveToDoCommandIsValid(RemoveToDoCommand command)
    {
        return
                Mono.fromCallable(() -> Objects.requireNonNull(command))
                        .map(v -> {

                            if (StringUtils.hasText(v.getToDoId()))
                                return v;

                            throw new IncorrectRemoveToDoCommandException("Incorrect To-Do id to remove");
                        });
    }

    private Mono<RemoveToDoResult> doRemoveToDo(RemoveToDoCommand removeToDoCommand)
    {
        return
                getToDoById(removeToDoCommand.getToDoId())
                        .flatMap(this::deleteToDo)
                        .map(this::toRemoveToDoResult);
    }

    private RemoveToDoResult toRemoveToDoResult(ToDo toDo)
    {
        return new RemoveToDoResult(mapper.map(toDo, ToDoDto.class));
    }

    private Mono<ToDo> deleteToDo(ToDo toDo)
    {
        return
                Mono
                        .fromCallable(
                                () -> {
                                    toDoRepository.delete(toDo);

                                    return toDo;
                                }
                        )
                        .onErrorResume(
                                OptimisticLockingFailureException.class,
                                this::wrapException
                        );
    }

    private Mono<ToDo> saveToDo(ToDo toDo)
    {
        return
                Mono
                    .fromCallable(() -> toDoRepository.save(toDo))
                    .onErrorResume(
                            OptimisticLockingFailureException.class,
                            this::wrapException
                    );
    }

    private <T> Mono<T> wrapException(OptimisticLockingFailureException exception)
    {
        return
                Mono.error(
                    new SavingToDoException(
                            "Attempt to simultaneously work with To-Do against to other clients"
                    )
                );
    }
}
