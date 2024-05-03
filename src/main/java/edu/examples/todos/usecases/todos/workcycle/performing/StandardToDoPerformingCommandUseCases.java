package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.accounting.todos.ToDoAccountingService;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.common.exceptions.translation.ExceptionTranslator;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.IncorrectPerformToDoCommandException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StandardToDoPerformingCommandUseCases implements ToDoPerformingCommandUseCases
{
    private final ToDoAccountingService toDoAccountingService;

    private final ToDoRepository toDoRepository;

    private final UseCaseMapper mapper;

    private final ExceptionTranslator exceptionTranslator;

    @Override
    @Transactional
    public Mono<PerformToDoResult> performToDo(PerformToDoCommand command)
    {
        return
                ensurePerformToDoCommandIsValid(command)
                        .flatMap(this::doPerformToDo);
    }

    private Mono<PerformToDoCommand> ensurePerformToDoCommandIsValid(PerformToDoCommand command)
    {
        return 
                Mono.fromCallable(() -> {

                    Objects.requireNonNull(command);
                    
                    if (StringUtils.hasText(command.getToDoId()))
                        return command;
                    
                    throw new IncorrectPerformToDoCommandException();
                });
    }

    private Mono<PerformToDoResult> doPerformToDo(PerformToDoCommand command)
    {
        return
                getToDoByIdToPerform(command.getToDoId())
                    .map(v -> {

                        v.perform();

                        return v;
                    })
                    .flatMap(this::saveToDo)
                    .flatMap(this::toPerformToDoResult);
    }

    private Mono<OperableToDo> getToDoByIdToPerform(String toDoId)
    {
        return
                toDoAccountingService.getToDoByIdForPerforming(ToDoId.of(toDoId))
                    .onErrorResume(
                        Exception.class,
                        e -> Mono.error(exceptionTranslator.translateException(e))
                    );
    }

    private Mono<PerformToDoResult> toPerformToDoResult(OperableToDo toDo)
    {
        return
                toDoAccountingService
                    .toOperableToDoAsync(toDo.getTarget())
                    .map(v -> new PerformToDoResult(mapper.map(v, ToDoDto.class)));
    }

    private Mono<OperableToDo> saveToDo(OperableToDo toDo)
    {
        return Mono.fromCallable(() -> toDoRepository.save(toDo));
    }
}
