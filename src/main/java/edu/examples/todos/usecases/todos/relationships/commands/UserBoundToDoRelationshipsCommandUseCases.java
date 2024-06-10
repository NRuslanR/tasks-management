package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.availability.todos.UserToDoActionsService;
import edu.examples.todos.usecases.common.annotations.ConditionalOnUserBinding;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentResult;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.IncorrectAssignToDoParentCommandException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
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
public class UserBoundToDoRelationshipsCommandUseCases implements ToDoRelationshipsCommandUseCases
{
    private final CurrentUserService currentUserService;
    private final UserToDoActionsService userToDoActionsService;
    private final ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases;

    @Override
    public Mono<AssignToDoParentResult> assignToDoParent(@Valid AssignToDoParentCommand command)
            throws NullPointerException, IncorrectAssignToDoParentCommandException, ToDoNotFoundException, ToDoIsInCorrectToBeParentException
    {
        return
                userToDoActionsService
                        .ensureToDoParentAssigningAvailableForUserAsync(
                                ToDoId.of(command.getTargetToDoId()),
                                currentUserService.getCurrentUserId()
                        )
                        .zipWhen(v -> toDoRelationshipsCommandUseCases.assignToDoParent(command))
                        .map(
                                v -> AssignToDoParentResult.of(
                                        v.getT2().getToDo().combineWithActionsAvailabilityByPairwiseAnd(v.getT1())
                                )
                        );
    }
}
