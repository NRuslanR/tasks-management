package edu.examples.todos.usecases.todos.workcycle.performing;

import edu.examples.todos.usecases.todos.common.behaviour.states.ToDoStateUtilService;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.dtos.ToDoActionsAvailabilityDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoStateIsNotCorrectException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.IncorrectPerformToDoCommandException;
import edu.examples.todos.usecases.todos.workcycle.performing.perform.PerformToDoCommand;
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

    private final ToDoStateUtilService toDoStateUtilService;

    private final ToDoPerformingCommandUseCases toDoPerformingCommandUseCases;

    @Test
    public void should_Perform_ToDo_When_Command_Is_Correct_And_ToDoExists_And_ToDoState_Is_Correct()
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
                assertEquals("performed", performedToDo.getState());
                assertTrue(StringUtils.hasText(performedToDo.getDisplayState()));
                assertNotNull(performedToDo.getPerformedAt());

                var actionsAvailability = performedToDo.getActionsAvailability();

                assertEquals(ToDoActionsAvailabilityDto.onlyViewingAvailable(), actionsAvailability);
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

    @ParameterizedTest
    @MethodSource("getIncorrectToDoStateIdsForPerforming")
    public void should_ThrowException_When_ToDoState_IsNot_Correct_To_Be_Performed(String incorrectToDoStateId)
    {
        if (!StringUtils.hasText(incorrectToDoStateId))
            return;

        var toDo = toDoCreationUtilService.createRandomToDo();

        toDoStateUtilService.setToDoState(toDo, incorrectToDoStateId);

        var command =
            ToDoPerformingCommandUseCasesTestsUtils
                .createCommandForToDoPerforming(toDo.getId());

        var result = toDoPerformingCommandUseCases.performToDo(command);

        StepVerifier
                .create(result)
                .expectError(ToDoStateIsNotCorrectException.class)
                .verify();
    }

    public Stream<Arguments> getIncorrectToDoStateIdsForPerforming()
    {
        return toDoStateUtilService.getIncorrectToDoStatesForPerform().stream().map(Arguments::of);
    }
}
