package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDoException;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.common.exceptions.DomainException;
import edu.examples.todos.domain.operations.accounting.todos.ToDoAccountingService;
import edu.examples.todos.domain.operations.creation.todos.*;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.common.exceptions.translation.ExceptionTranslator;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.create.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.IncorrectRemoveToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.update.IncorrectUpdateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoResult;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandardToDoAccountingCommandUseCases implements ToDoAccountingCommandUseCases
{
    private final ToDoCreationService toDoCreationService;
    private final ToDoAccountingService toDoAccountingService;

    private final UseCaseMapper mapper;

    private final ToDoRepository toDoRepository;

    private final ExceptionTranslator exceptionTranslator;

    @Override
    @Transactional
    public Mono<CreateToDoResult> createToDo(@Valid CreateToDoCommand command)
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
        return Mono.fromCallable(
                () -> {
                    var cmd = Objects.requireNonNull(command);

                    throwIfPriorityTypeValueCombinationIsInCorrect(cmd.getPriorityType(), cmd.getPriorityValue());

                    return cmd;
                }
        );
    }

    private Mono<CreateToDoRequest> toCreateToDoRequest(CreateToDoCommand createToDoCommand)
    {
        return
                Mono
                    .fromCallable(() -> mapper.map(createToDoCommand, CreateToDoRequest.class))
                        .onErrorResume(
                                DomainException.class,
                                e -> Mono.error(new IncorrectCreateToDoCommandException(e.getMessage()))
                        );
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
                    .map(CreateToDoReply::of);
    }

    private Mono<CreateToDoResult> toCreateToDoCommandResultAsync(CreateToDoReply reply)
    {
        return Mono.fromCallable(() -> CreateToDoResult.of(mapper.map(reply.getToDo(), ToDoDto.class)));
    }

    @Override
    @Transactional
    public Mono<UpdateToDoResult> updateToDo(
            @Valid UpdateToDoCommand updateToDoCommand
    )
        throws
            NullPointerException,
            IncorrectUpdateToDoCommandException,
            ToDoNotFoundException
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

                            throwIfPriorityTypeValueCombinationIsInCorrect(
                                    v.getPriorityType(), v.getPriorityValue()
                            );

                            throw new IncorrectUpdateToDoCommandException("Incorrect To-Do id to update");

                        });
    }

    private Mono<UpdateToDoResult> doUpdateToDo(UpdateToDoCommand updateToDoCommand)
    {
        return
                getToDoByIdToUpdate(updateToDoCommand.getToDoId())
                    .map(v -> mapper.map(updateToDoCommand, v))
                    .onErrorResume(
                            ToDoException.class,
                            e -> Mono.error(new IncorrectUpdateToDoCommandException(e.getMessage()))
                    )
                    .flatMap(this::saveToDo)
                    .flatMap(this::toUpdateToDoResult);
    }

    private Mono<OperableToDo> getToDoByIdToUpdate(String toDoId)
    {
        return
                toDoAccountingService
                        .getToDoByIdForChanging(ToDoId.of(toDoId))
                        .onErrorResume(
                                Exception.class,
                                e -> Mono.error(exceptionTranslator.translateException(e))
                        );
    }

    private Mono<UpdateToDoResult> toUpdateToDoResult(OperableToDo operableToDo)
    {
        return
                toDoAccountingService
                        .toOperableToDoAsync(operableToDo.getTarget())
                        .map(v -> UpdateToDoResult.of(mapper.map(v, ToDoDto.class)));
    }

    @Override
    @Transactional
    public Mono<RemoveToDoResult> removeToDo(@Valid RemoveToDoCommand command)
            throws
            NullPointerException,
            IncorrectRemoveToDoCommandException,
            ToDoNotFoundException
    {
        return
                ensureRemoveToDoCommandIsValid(command)
                    .flatMap(this::doRemoveToDo);
    }

    private Mono<RemoveToDoCommand> ensureRemoveToDoCommandIsValid(RemoveToDoCommand command)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(command))
                    .map(v -> {

                        if (StringUtils.hasText(v.getToDoId()))
                            return v;

                        throw new IncorrectRemoveToDoCommandException("Incorrect To-Do id to remove");
                    });
    }

    private Mono<RemoveToDoResult> doRemoveToDo(RemoveToDoCommand removeToDoCommand)
    {
        return
                getToDoByIdToRemove(removeToDoCommand.getToDoId())
                    .flatMap(this::deleteToDo)
                    .map(this::toRemoveToDoResult);
    }

    private Mono<OperableToDo> getToDoByIdToRemove(String toDoId)
    {
        return
                toDoAccountingService
                    .getToDoByIdForRemoving(ToDoId.of(toDoId))
                    .onErrorResume(
                        Exception.class,
                        e -> Mono.error(exceptionTranslator.translateException(e))
                    );
    }

    private RemoveToDoResult toRemoveToDoResult(OperableToDo toDo)
    {
        return RemoveToDoResult.of(mapper.map(toDo, ToDoDto.class));
    }

    private Mono<OperableToDo> deleteToDo(OperableToDo toDo)
    {
        return Mono.fromCallable(() -> toDoRepository.deleteRecursively(toDo));
    }

    private Mono<OperableToDo> saveToDo(OperableToDo toDo)
    {
        return Mono.fromCallable(() -> toDoRepository.save(toDo));
    }

    private void throwIfPriorityTypeValueCombinationIsInCorrect(String priorityType, Optional<Integer> priorityValue)
    {
        var toDoPriorityInfoAssigned =
                StringUtils.hasText(priorityType) && !Objects.isNull(priorityValue) && priorityValue.isPresent();

        var FullToDoPriorityInfoNotAssigned =
                !StringUtils.hasText(priorityType) && Objects.isNull(priorityValue);

        if (!(toDoPriorityInfoAssigned || FullToDoPriorityInfoNotAssigned))
            throw new IncorrectCreateToDoCommandException("Incorrect priority type-value combination");
    }
}
