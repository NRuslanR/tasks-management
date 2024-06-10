package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.operations.availability.todos.UserToDoActionsService;
import edu.examples.todos.usecases.common.annotations.ConditionalOnUserBinding;
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
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.users.accounting.services.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@ConditionalOnUserBinding
@Service
@RequiredArgsConstructor
@Transactional
public class UserBoundToDoAccountingCommandUseCases implements ToDoAccountingCommandUseCases
{
    private final CurrentUserService currentUserService;
    private final UserToDoActionsService userToDoActionsService;
    private final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    @Override
    public Mono<CreateToDoResult> createToDo(@Valid CreateToDoCommand command)
            throws NullPointerException, IncorrectCreateToDoCommandException, ToDoAlreadyExistsException
    {
        return
                userToDoActionsService
                        .ensureUserCanCreateToDos(currentUserService.getCurrentUserId())
                        .map(v -> command.withAuthorId(v.getValue().toString()))
                        .flatMap(toDoAccountingCommandUseCases::createToDo);
    }

    @Override
    public Mono<UpdateToDoResult> updateToDo(@Valid UpdateToDoCommand updateToDoCommand)
            throws NullPointerException, IncorrectUpdateToDoCommandException, ToDoNotFoundException
    {
        return
                userToDoActionsService
                        .ensureToDoChangingAvailableForUserAsync(
                                ToDoId.of(updateToDoCommand.getToDoId()),
                                currentUserService.getCurrentUserId()
                        )
                        .zipWhen(v -> toDoAccountingCommandUseCases.updateToDo(updateToDoCommand))
                        .map(
                                v -> UpdateToDoResult.of(
                                        v.getT2().getToDo().combineWithActionsAvailabilityByPairwiseAnd(v.getT1())
                                )
                        );
    }

    @Override
    public Mono<RemoveToDoResult> removeToDo(@Valid RemoveToDoCommand command)
            throws NullPointerException, IncorrectRemoveToDoCommandException, ToDoNotFoundException
    {
        return
                userToDoActionsService
                        .ensureToDoRemovingAvailableForUserAsync(
                                ToDoId.of(command.getToDoId()),
                                currentUserService.getCurrentUserId()
                        )
                        .zipWhen(v -> toDoAccountingCommandUseCases.removeToDo(command))
                        .map(
                                v -> RemoveToDoResult.of(
                                        v.getT2().getToDo().combineWithActionsAvailabilityByPairwiseAnd(v.getT1())
                                )
                        );
    }
}
