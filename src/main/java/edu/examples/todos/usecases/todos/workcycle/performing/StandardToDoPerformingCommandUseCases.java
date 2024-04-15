package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.actors.todos.ToDoStatusIsNotCorrectDomainException;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.IncorrectPerformToDoCommandException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoResult;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.ToDoStatusIsNotCorrectException;
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
    private final ToDoRepository toDoRepository;

    private final UseCaseMapper mapper;

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
                getToDoById(command.getToDoId())
                        .map(v -> {

                            v.perform();

                            return v;
                        })
                        .onErrorResume(
                            ToDoStatusIsNotCorrectDomainException.class,
                            e -> Mono.error(new ToDoStatusIsNotCorrectException(e.getMessage()))
                        )
                        .flatMap(this::saveToDo)
                        .flatMap(this::toPerformToDoResult);
    }

    private Mono<ToDo> getToDoById(String toDoId)
    {
        return
                Mono.fromCallable(
                        () ->
                            toDoRepository
                                .findById(ToDoId.of(toDoId))
                                .orElseThrow(ToDoNotFoundException::new)
                );
    }

    private Mono<ToDo> saveToDo(ToDo toDo)
    {
        return Mono.fromCallable(() -> toDoRepository.save(toDo));
    }

    private Mono<PerformToDoResult> toPerformToDoResult(ToDo toDo)
    {
        return Mono.fromCallable(() -> new PerformToDoResult(mapper.map(toDo, ToDoDto.class)));
    }
}
