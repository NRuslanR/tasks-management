package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.relationships.todos.DescendentToDoCanNotBeParentForAncestorToDoException;
import edu.examples.todos.domain.operations.relationships.todos.ToDoRelationshipsService;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentResult;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.IncorrectAssignToDoParentCommandException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoRelationshipsCommandUseCases implements ToDoRelationshipsCommandUseCases
{
    private final ToDoRelationshipsService toDoRelationshipsService;

    private final ToDoRepository toDoRepository;

    private final UseCaseMapper mapper;

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
                Mono.zip(
                        v -> new Pair<>((ToDo) v[0], (ToDo) v[1]),

                        getToDoById(assignToDoParentCommand.getTargetToDoId())
                                .subscribeOn(Schedulers.boundedElastic()),

                        getToDoById(assignToDoParentCommand.getParentToDoId())
                                .subscribeOn(Schedulers.boundedElastic())
                )
                .flatMap(v ->
                        toDoRelationshipsService.assignToDoParentAsync(
                                Objects.equals(v.getValue0().getId().asString(), assignToDoParentCommand.getTargetToDoId()) ?
                                        v.getValue0() : v.getValue1(),

                                Objects.equals(v.getValue0().getId().asString(), assignToDoParentCommand.getParentToDoId()) ?
                                        v.getValue0() : v.getValue1()
                        )
                )
                .flatMap(this::saveToDo)
                .onErrorResume(
                        DescendentToDoCanNotBeParentForAncestorToDoException.class,
                        v -> Mono.error(new ToDoIsInCorrectToBeParentException(v.getMessage()))
                )
                .map(v -> new AssignToDoParentResult(mapper.map(v, ToDoDto.class)));
    }

    /* refactor: duplicate getToDoById in StandardToDoAccountingCommandUseCases */
    private Mono<ToDo> getToDoById(String targetToDoId)
    {
        return
                Mono.fromCallable(
                        () ->
                            toDoRepository
                                .findById(ToDoId.of(targetToDoId))
                                .orElseThrow(ToDoNotFoundException::new)
                );
    }

    private Mono<ToDo> saveToDo(ToDo toDo)
    {
        return Mono.fromCallable(() -> toDoRepository.save(toDo));
    }
}
