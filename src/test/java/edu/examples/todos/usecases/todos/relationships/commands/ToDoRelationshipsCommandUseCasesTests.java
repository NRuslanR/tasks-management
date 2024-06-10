package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils;
import edu.examples.todos.usecases.todos.common.behaviour.states.ToDoStateUtilService;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoStateIsNotCorrectException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.IncorrectAssignToDoParentCommandException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public abstract class ToDoRelationshipsCommandUseCasesTests
{
    private final ToDoCreationUtilService toDoCreationUtilService;

    private final ToDoStateUtilService toDoStateUtilService;

    private final ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases;

    @Test
    public void should_AssignToDoParent_When_Command_And_ParentToDo_And_BothToDoStates_Are_Correct()
    {
        var targetToDoId = toDoCreationUtilService.createRandomToDo().getId();
        var parentToDoId = toDoCreationUtilService.createRandomToDo().getId();

        var command =
                ToDoAccountingCommandUseCasesTestsUtils
                        .createCommandForToDoParentAssigning(targetToDoId, parentToDoId);

        var result = toDoRelationshipsCommandUseCases.assignToDoParent(command);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var toDoDto = v.getToDo();

                    assertNotNull(toDoDto);

                    assertEquals(command.getParentToDoId(), toDoDto.getParentToDoId());
                })
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("createIncorrectAssignToDoParentCommands")
    public void should_ThrowException_When_AssignToDoParentCommand_IsInCorrect(AssignToDoParentCommand incorrectCommand)
    {
        var result = toDoRelationshipsCommandUseCases.assignToDoParent(incorrectCommand);

        StepVerifier
                .create(result)
                .expectError(IncorrectAssignToDoParentCommandException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectAssignToDoParentCommands()
    {
            return
                    Stream.of(
                            Arguments.of(new AssignToDoParentCommand()),
                            Arguments.of(AssignToDoParentCommand.of(null, generateRandomToDoId())),
                            Arguments.of(AssignToDoParentCommand.of(generateRandomToDoId(), null))
                    );
    }

    @Test
    public void should_ThrowException_When_TargetOrParentToDos_NotFound()
    {
        var command = AssignToDoParentCommand.of(generateRandomToDoId(), generateRandomToDoId());

        var result = toDoRelationshipsCommandUseCases.assignToDoParent(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    @Test
    public void should_ThrowException_When_AssigningDescendentToDo_As_Parent_For_AncestorToDo()
    {
        var firstId = toDoCreationUtilService.createRandomToDo().getId();
        var secondId = toDoCreationUtilService.createRandomToDo().getId();
        var thirdId = toDoCreationUtilService.createRandomToDo().getId();

        var result =
            toDoRelationshipsCommandUseCases
                    .assignToDoParent(AssignToDoParentCommand.of(secondId, firstId))
                    .then(
                            Mono.defer(
                                    () ->
                                        toDoRelationshipsCommandUseCases.assignToDoParent(
                                                AssignToDoParentCommand.of(thirdId, secondId)
                                        )
                            )
                    )
                    .then(
                            Mono.defer(
                                    () ->
                                        toDoRelationshipsCommandUseCases.assignToDoParent(
                                                AssignToDoParentCommand.of(firstId, thirdId)
                                        )
                            )
                    );

        StepVerifier
                .create(result)
                .expectError(ToDoIsInCorrectToBeParentException.class)
                .verify();
    }

    @ParameterizedTest
    @MethodSource("getIncorrectToDoStateIdsForParentAssigning")
    public void should_ThrowException_When_TargetOrParentToDoStates_AreNot_Correct_For_ParentAssigning(Pair<String, String> incorrectToDoStateIds)
    {
        if (Objects.isNull(incorrectToDoStateIds))
            return;

        var incorrectTargetToDoStateId = incorrectToDoStateIds.getValue0();
        var incorrectParentToDoStateId = incorrectToDoStateIds.getValue1();

        var targetToDo = toDoCreationUtilService.createRandomToDo();
        var parentToDo = toDoCreationUtilService.createRandomToDo();

        if (StringUtils.hasText(incorrectTargetToDoStateId))
            toDoStateUtilService.setToDoState(targetToDo, incorrectTargetToDoStateId);

        if (StringUtils.hasText(incorrectParentToDoStateId))
            toDoStateUtilService.setToDoState(parentToDo, incorrectParentToDoStateId);

        var command = AssignToDoParentCommand.of(targetToDo.getId(), parentToDo.getId());

        var result = toDoRelationshipsCommandUseCases.assignToDoParent(command);

        StepVerifier
                .create(result)
                .expectError(ToDoStateIsNotCorrectException.class)
                .verify();
    }

    public Stream<Arguments> getIncorrectToDoStateIdsForParentAssigning()
    {
        return toDoStateUtilService.getIncorrectToDoStatesForParentAssigning().stream().map(Arguments::of);
    }
}
