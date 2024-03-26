package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.IncorrectUpdateToDoCommandException;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
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

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils.createSimpleIncorrectCommandForToDoCreating;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ToDoAccountingCommandUseCasesTests
{
    protected final ToDoAccountingCommandUseCases toDoAccountingCommandUseCases;

    private enum ToDoNameUseCases { CREATE, UPDATE, UPDATE_WITH_INCORRECT_COMMAND, REMOVE, ALREADY_EXISTS }

    private Map<ToDoNameUseCases, String> testToDoNames =
            Map.of(
                    ToDoNameUseCases.CREATE, "To-Do#1",
                    ToDoNameUseCases.UPDATE, "To-Do#2",
                    ToDoNameUseCases.UPDATE_WITH_INCORRECT_COMMAND, "To-Do#3",
                    ToDoNameUseCases.REMOVE, "To-Do#4",
                    ToDoNameUseCases.ALREADY_EXISTS, "To-Do#5"
            );

    @Test
    public void should_Create_ToDo_When_CreateToDoCommand_IsCorrect_And_ToDoDoesNotExistsYet()
    {
        var commandResultPair = createToDo(testToDoNames.get(ToDoNameUseCases.CREATE));

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
        var toDoName = testToDoNames.get(ToDoNameUseCases.ALREADY_EXISTS);

        var result =
                createToDo(toDoName)
                        .getValue()
                        .then(
                                Mono.defer(
                                        () -> createToDo(toDoName).getValue()
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
        var toDoName = testToDoNames.get(ToDoNameUseCases.UPDATE);

        var createToDoResult = createToDo(toDoName).getValue().block();

        var updateToDoCommand =
                ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoUpdating(
                        createToDoResult.getToDo().getId()
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
        var toDoId =
            createToDo(testToDoNames.get(ToDoNameUseCases.UPDATE_WITH_INCORRECT_COMMAND))
                .getValue()
                .block()
                .getToDo()
                .getId();


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

    private KeyValue<CreateToDoCommand, Mono<CreateToDoResult>> createToDo(String toDoName)
    {
        var command = ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoCreating(toDoName);

        var result = toDoAccountingCommandUseCases.createToDo(command);

        return KeyValue.with(command, result);
    }
}
