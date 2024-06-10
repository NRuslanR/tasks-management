package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.availability.todos.UserToDoActionsService;
import edu.examples.todos.usecases.common.annotations.ConditionalOnUserBinding;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoResult;
import edu.examples.todos.usecases.users.accounting.services.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@ConditionalOnUserBinding
@RequiredArgsConstructor
@Service
@Transactional
public class UserBoundToDoPerformingCommandUseCases implements ToDoPerformingCommandUseCases
{
    private final CurrentUserService currentUserService;
    private final UserToDoActionsService userToDoActionsService;
    private final ToDoPerformingCommandUseCases toDoPerformingCommandUseCases;

    @Override
    public Mono<PerformToDoResult> performToDo(@Valid PerformToDoCommand command)
    {
        return
                userToDoActionsService
                        .ensureToDoPerformingAvailableForUserAsync(
                                ToDoId.of(command.getToDoId()),
                                currentUserService.getCurrentUserId()
                        )
                        .zipWhen(v -> toDoPerformingCommandUseCases.performToDo(command))
                        .map(
                                v ->
                                    PerformToDoResult.of(
                                            v.getT2().getToDo().combineWithActionsAvailabilityByPairwiseAnd(v.getT1())
                                    )
                        );
    }
}
