package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.create.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.IncorrectRemoveToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.remove.RemoveToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.IncorrectUpdateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.javatuples.KeyValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils.createSimpleIncorrectCommandForToDoCreating;
import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoName;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public abstract class ToDoAccountingCommandUseCasesTests
{
    protected final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;
    
    @Test
    public void should_Create_ToDo_When_CreateToDoCommand_IsCorrect_And_ToDoDoesNotExistsYet()
    {
        var commandResultPair = runCreateRandomToDoCommandFor();

        var command = commandResultPair.getKey();
        var result = commandResultPair.getValue();

        StepVerifier
            .create(result)
            .assertNext(v -> {

                assertNotNull(v);

                var toDoDto = v.getToDo();

                assertNotNull(toDoDto);
                assertTrue(StringUtils.doesNotContainWhitespace(toDoDto.getId()));
                assertEquals(command.getName(), toDoDto.getName());
                assertEquals(command.getDescription(), toDoDto.getDescription());
                assertNotNull(toDoDto.getCreatedAt());
            })
            .verifyComplete();
    }

    @Test
    public void should_ThrowException_When_CreateToDoCommand_IsNotCorrect()
    {
        var incorrectCreateToDoCommand = createSimpleIncorrectCommandForToDoCreating();

        var createToDoResult = toDoAccountingCommandUseCases.createToDo(incorrectCreateToDoCommand);

        StepVerifier
            .create(createToDoResult)
            .expectError(IncorrectCreateToDoCommandException.class)
            .verify();
    }

    @Test
    public void should_ThrowException_When_ToDoAlreadyExists()
    {
        var toDoName = generateRandomToDoName();

        var result =
                runCreateToDoCommandFor(toDoName)
                        .getValue()
                        .then(
                                Mono.defer(
                                        () -> runCreateToDoCommandFor(toDoName).getValue()
                                )
                        );

        StepVerifier
                .create(result)
                .expectError(ToDoAlreadyExistsException.class)
                .verify();
    }

    @Test
    public void should_UpdateToDo_When_UpdateToDoCommand_IsCorrect_And_ToDoExists()
    {
        var updateToDoCommand =
                ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoUpdating(
                        createToDo(generateRandomToDoName()).getId()
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
        var toDoId = createToDo(generateRandomToDoName()).getId();

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
                        .createSimpleCommandForToDoUpdating(UUID.randomUUID().toString());

        var result = toDoAccountingCommandUseCases.updateToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    @Test
    public void should_RemoveToDo_When_RemoveToDoCommand_IsCorrect_And_ToDoExists()
    {
        var toDoId = createToDo(generateRandomToDoName()).getId();

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
                        .createCommandForToDoRemoving(UUID.randomUUID().toString());

        var result = toDoAccountingCommandUseCases.removeToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    public ToDoDto createToDo(String toDoName)
    {
        return
                runCreateToDoCommandFor(toDoName)
                        .getValue()
                        .block()
                        .getToDo();
    }

    private KeyValue<CreateToDoCommand, Mono<CreateToDoResult>> runCreateRandomToDoCommandFor()
    {
        return runCreateToDoCommandFor(generateRandomToDoName());
    }

    private KeyValue<CreateToDoCommand, Mono<CreateToDoResult>> runCreateToDoCommandFor(String toDoName)
    {
        var command = ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoCreating(toDoName);

        var result = toDoAccountingCommandUseCases.createToDo(command);

        return KeyValue.with(command, result);
    }
}
