package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.accounting.todos.ToDoAccountingService;
import edu.examples.todos.domain.operations.relationships.todos.ToDoRelationshipsService;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.common.exceptions.translation.ExceptionTranslator;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentResult;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.IncorrectAssignToDoParentCommandException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoRelationshipsCommandUseCases implements ToDoRelationshipsCommandUseCases
{
    private final ToDoRelationshipsService toDoRelationshipsService;

    private final ToDoAccountingService toDoAccountingService;

    private final ToDoRepository toDoRepository;

    private final UseCaseMapper mapper;

    private final ExceptionTranslator exceptionTranslator;

    @Override
    @Transactional
    public Mono<AssignToDoParentResult> assignToDoParent(AssignToDoParentCommand command)
            throws
            NullPointerException,
            IncorrectAssignToDoParentCommandException,
            ToDoNotFoundException,
            ToDoIsInCorrectToBeParentException
    {
        return
                ensureAssignToDoParentIsValid(command)
                        .flatMap(this::doAssignToDoParent);
    }

    private Mono<AssignToDoParentCommand> ensureAssignToDoParentIsValid(AssignToDoParentCommand command)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(command))
                    .map(v -> {

                        if (
                            StringUtils.hasText(v.getTargetToDoId()) &&
                                    StringUtils.hasText(command.getParentToDoId())
                        )
                        {
                            return v;
                        }

                        throw new IncorrectAssignToDoParentCommandException("Incorrect To-Do id");
                    });
    }

    private Mono<AssignToDoParentResult> doAssignToDoParent(AssignToDoParentCommand assignToDoParentCommand)
    {
        return
                toDoRelationshipsService.assignToDoParentAsync(
                        ToDoId.of(assignToDoParentCommand.getTargetToDoId()),
                        ToDoId.of(assignToDoParentCommand.getParentToDoId())
                )
                .onErrorResume(
                        Exception.class,
                        e -> {
                            var frontException = exceptionTranslator.translateException(e);

                            return Mono.error(
                                    frontException.equals(e) ?
                                        new IncorrectAssignToDoParentCommandException(e.getMessage()) :
                                        frontException
                            );
                        }
                )
                .flatMap(this::saveToDo)
                .flatMap(this::toAssignToDoParentResult);
    }

    private Mono<AssignToDoParentResult> toAssignToDoParentResult(OperableToDo operableToDo)
    {
        return
                toDoAccountingService
                        .toOperableToDoAsync(operableToDo.getTarget())
                        .map(v -> new AssignToDoParentResult(mapper.map(v, ToDoDto.class)));
    }

    private Mono<OperableToDo> saveToDo(OperableToDo toDo)
    {
        return Mono.fromCallable(() -> toDoRepository.save(toDo));
    }
}
