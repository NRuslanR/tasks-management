package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.domain.actors.todos.ToDoPriorityType;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.create.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.IncorrectRemoveToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.IncorrectUpdateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.common.behaviour.states.ToDoStateUtilService;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoStateIsNotCorrectException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils.createSimpleIncorrectCommandForToDoCreating;
import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoId;
import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoName;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public abstract class ToDoAccountingCommandUseCasesTests
{
    protected final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    protected final UtilToDoAccountingCommandUseCases utilToDoAccountingCommandUseCases;

    protected final ToDoCreationUtilService toDoCreationUtilService;

    protected final ToDoStateUtilService toDoStateUtilService;

    @AfterAll
    public void clearFixtureForAll()
    {

    }

    @Test
    public void should_Create_ToDo_When_CreateToDoCommand_IsCorrect_And_ToDoDoesNotExistsYet()
    {
        var commandResultPair =
                utilToDoAccountingCommandUseCases.runCreateRandomToDoCommand();

        var command = commandResultPair.getKey();
        var result = commandResultPair.getValue();

        StepVerifier
            .create(result)
            .assertNext(v -> {

                assertNotNull(v);

                var toDoDto = v.getToDo();

                assertNotNull(toDoDto);

                assertFalse(StringUtils.containsWhitespace(toDoDto.getId()));
                assertEquals(command.getName(), toDoDto.getName());
                assertEquals(command.getDescription(), toDoDto.getDescription());
                assertNotNull(toDoDto.getCreatedAt());

                var actionsAvailability = toDoDto.getActionsAvailability();

                assertNotNull(actionsAvailability);

                assertTrue(actionsAvailability.isViewingAvailable());
                assertTrue(actionsAvailability.isChangingAvailable());
                assertTrue(actionsAvailability.isRemovingAvailable());
                assertTrue(actionsAvailability.isParentAssigningAvailable());
                assertTrue(actionsAvailability.isPerformingAvailable());
            })
            .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("createIncorrectCommandsForToDoCreating")
    public void should_ThrowException_When_CreateToDoCommand_IsNotCorrect()
    {
        var incorrectCreateToDoCommand = createSimpleIncorrectCommandForToDoCreating();

        var createToDoResult = toDoAccountingCommandUseCases.createToDo(incorrectCreateToDoCommand);

        StepVerifier
            .create(createToDoResult)
            .expectError(IncorrectCreateToDoCommandException.class)
            .verify();
    }

    public Stream<Arguments> createIncorrectCommandsForToDoCreating()
    {
        return Stream.of(
                Arguments.of(createSimpleIncorrectCommandForToDoCreating()),
                Arguments.of(new CreateToDoCommand(generateRandomToDoName(), "", "", Optional.of(5))),
                Arguments.of(new CreateToDoCommand(generateRandomToDoName(), "", ToDoPriorityType.MEDIUM.toString(), null)),
                Arguments.of(new CreateToDoCommand(generateRandomToDoName(), "", ToDoPriorityType.MEDIUM.toString(), Optional.empty()))
        );
    }

    @Test
    public void should_ThrowException_When_ToDoAlreadyExists_To_Be_Created()
    {
        var toDoName = generateRandomToDoName();

        var result =
                utilToDoAccountingCommandUseCases
                        .runCreateToDoCommandFor(toDoName)
                        .getValue()
                        .then(
                                Mono.defer(
                                        () ->
                                            utilToDoAccountingCommandUseCases
                                                .runCreateToDoCommandFor(toDoName)
                                                .getValue()
                                )
                        );

        StepVerifier
                .create(result)
                .expectError(ToDoAlreadyExistsException.class)
                .verify();
    }

    @Test
    public void should_UpdateToDo_When_UpdateToDoCommand_IsCorrect_And_ToDoExists_And_ToDoState_IsCorrect()
    {
        var updateToDoCommand =
                ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoUpdating(
                        toDoCreationUtilService.createRandomToDo().getId()
                );

        var updateToDoResult = toDoAccountingCommandUseCases.updateToDo(updateToDoCommand);

        StepVerifier
                .create(updateToDoResult)
                .assertNext(v -> {

                    assertNotNull(v);

                    var toDoDto = v.getToDo();

                    assertNotNull(toDoDto);
                    assertEquals(updateToDoCommand.getToDoId(), toDoDto.getId());
                    assertEquals(updateToDoCommand.getName(), toDoDto.getName());
                    assertEquals(updateToDoCommand.getDescription(), toDoDto.getDescription());
                })
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("createIncorrectCommandsForToDoUpdating")
    public void should_ThrowException_When_UpdateToDoCommand_IsInCorrect(UpdateToDoCommand incorrectCommand)
    {
        var result = toDoAccountingCommandUseCases.updateToDo(incorrectCommand);

        StepVerifier
                .create(result)
                .expectError(IncorrectUpdateToDoCommandException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectCommandsForToDoUpdating()
    {
        var toDoId = toDoCreationUtilService.createRandomToDo().getId();

        return Stream.of(
                Arguments.of(
                        ToDoAccountingCommandUseCasesTestsUtils.createSimpleIncorrectCommandForToDoUpdating()
                ),
                Arguments.of(
                        ToDoAccountingCommandUseCasesTestsUtils.createSimpleIncorrectCommandForToDoUpdating(toDoId)
                )
        );
    }

    @Test
    public void should_ThrowException_When_ToDoNotFound_ToBe_Updated()
    {
        var command =
                ToDoAccountingCommandUseCasesTestsUtils
                        .createSimpleCommandForToDoUpdating(generateRandomToDoId());

        var result = toDoAccountingCommandUseCases.updateToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    @ParameterizedTest
    @MethodSource("getIncorrectToDoStateIdsForUpdate")
    public void should_ThrowException_When_ToDoSate_IsInCorrect_To_Be_Updated(String incorrectToDoStateId)
    {
        if (!StringUtils.hasText(incorrectToDoStateId))
            return;

        var toDo = toDoCreationUtilService.createRandomToDo();

        toDoStateUtilService.setToDoState(toDo, incorrectToDoStateId);

        var command =
            ToDoAccountingCommandUseCasesTestsUtils
                .createSimpleCommandForToDoUpdating(toDo.getId());

        var result= toDoAccountingCommandUseCases.updateToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoStateIsNotCorrectException.class)
                .verify();
    }

    public Stream<Arguments> getIncorrectToDoStateIdsForUpdate()
    {
        return toDoStateUtilService.getIncorrectToDoStatesForUpdate().stream().map(Arguments::of);
    }

    @Test
    public void should_RemoveToDo_When_RemoveToDoCommand_IsCorrect_And_ToDoExists_And_ToDoState_Is_Correct()
    {
        var toDoId = toDoCreationUtilService.createRandomToDo().getId();

        var command = ToDoAccountingCommandUseCasesTestsUtils.createCommandForToDoRemoving(toDoId);

        var result = toDoAccountingCommandUseCases.removeToDo(command);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var toDo = v.getToDo();

                    assertEquals(toDoId, toDo.getId());

                    assertThrows(ToDoNotFoundException.class, () -> {

                        var updateCommand =
                                ToDoAccountingCommandUseCasesTestsUtils
                                        .createSimpleCommandForToDoUpdating(toDoId);

                        toDoAccountingCommandUseCases.updateToDo(updateCommand).block();
                    });
                })
                .verifyComplete();
    }

    @Test
    public void should_ThrowException_When_RemoveToDoCommand_IsInCorrect()
    {
        var incorrectCommand = new RemoveToDoCommand();

        var result = toDoAccountingCommandUseCases.removeToDo(incorrectCommand);

        StepVerifier
                .create(result)
                .expectError(IncorrectRemoveToDoCommandException.class)
                .verify();
    }

    @Test
    public void should_ThrowException_When_ToDoDoesNotExist_To_Be_Removed()
    {
        var command =
                ToDoAccountingCommandUseCasesTestsUtils
                        .createCommandForToDoRemoving(generateRandomToDoId());

        var result = toDoAccountingCommandUseCases.removeToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    @ParameterizedTest
    @MethodSource("getIncorrectToDoStateIdsForRemove")
    public void should_ThrowException_When_ToDoSate_IsInCorrect_To_Be_Removed(String incorrectToDoStateId)
    {
        if (!StringUtils.hasText(incorrectToDoStateId))
            return;

        var toDo = toDoCreationUtilService.createRandomToDo();

        toDoStateUtilService.setToDoState(toDo, incorrectToDoStateId);

        var command =
                ToDoAccountingCommandUseCasesTestsUtils
                    .createCommandForToDoRemoving(toDo.getId());

        var result = toDoAccountingCommandUseCases.removeToDo(command);

        StepVerifier
            .create(result)
            .expectError(ToDoStateIsNotCorrectException.class)
            .verify();
    }

    public Stream<Arguments> getIncorrectToDoStateIdsForRemove()
    {
        return toDoStateUtilService.getIncorrectToDoStatesForRemove().stream().map(Arguments::of);
    }
}
