package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.IncorrectPerformToDoCommandException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.ToDoStatusIsNotCorrectException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.StringUtils;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ToDoPerformingCommandUseCasesTests
{
    private final ToDoCreationUtilService toDoCreationUtilService;

    private final ToDoPerformingCommandUseCases toDoPerformingCommandUseCases;

    @Test
    public void should_Perform_ToDo_When_Command_Is_Correct_And_ToDoExists_And_ToDoIsAtAllowedWorkCycleStage()
    {
        var toDo = toDoCreationUtilService.createRandomToDo();

        var command = ToDoPerformingCommandUseCasesTestsUtils.createCommandForToDoPerforming(toDo.getId());

        var result = toDoPerformingCommandUseCases.performToDo(command);

        StepVerifier
            .create(result)
            .assertNext(v -> {

                assertNotNull(v);

                var performedToDo = v.getToDo();

                assertNotNull(performedToDo);
                assertEquals(toDo.getId(), performedToDo.getId());
                assertEquals("performed", performedToDo.getStatus());
                assertTrue(StringUtils.hasText(performedToDo.getDisplayStatus()));
                assertNotNull(performedToDo.getPerformedAt());
            })
            .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("createIncorrectPerformToDoCommands")
    public void should_ThrowException_When_PerformToDoCommand_Is_InCorrect(PerformToDoCommand incorrectCommand)
    {
        var result = toDoPerformingCommandUseCases.performToDo(incorrectCommand);

        StepVerifier
                .create(result)
                .expectError(IncorrectPerformToDoCommandException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectPerformToDoCommands()
    {
        return Stream.of(
                Arguments.of(new PerformToDoCommand()),
                Arguments.of(new PerformToDoCommand(" "))
        );
    }

    @Test
    public void should_ThrowException_When_ToDoNotFound_To_Be_Performed()
    {
        var command =
                ToDoPerformingCommandUseCasesTestsUtils
                        .createCommandForNonExistentToDoPerforming();

        var result = toDoPerformingCommandUseCases.performToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    @Test
    public void should_ThrowException_When_ToDoIsAlreadyPerformed_To_Be_Performed()
    {
        var toDo = toDoCreationUtilService.createRandomToDo();

        toDoPerformingCommandUseCases.performToDo(
            ToDoPerformingCommandUseCasesTestsUtils
                .createCommandForToDoPerforming(toDo.getId())
        ).block();

        var result =
                toDoPerformingCommandUseCases.performToDo(
                    ToDoPerformingCommandUseCasesTestsUtils
                        .createCommandForToDoPerforming(toDo.getId())
                );

        StepVerifier
                .create(result)
                .expectError(ToDoStatusIsNotCorrectException.class)
                .verify();
    }
}
