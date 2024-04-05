package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.IncorrectAssignToDoParentCommandException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoId;
import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public abstract class ToDoRelationshipsCommandUseCasesTests
{
    private final ToDoCreationUtilService toDoCreationUtilService;

    private final ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases;

    @Test
    public void should_AssignToDoParent_When_Command_And_ParentToDo_Are_Correct()
    {
        var targetToDoId = toDoCreationUtilService.createToDo(generateRandomToDoName()).getId();
        var parentToDoId = toDoCreationUtilService.createToDo(generateRandomToDoName()).getId();

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
                            Arguments.of(new AssignToDoParentCommand(null, generateRandomToDoId())),
                            Arguments.of(new AssignToDoParentCommand(generateRandomToDoId(), null))
                    );
    }

    @Test
    public void should_ThrowException_When_TargetOrParentToDos_NotFound()
    {
        var command = new AssignToDoParentCommand(generateRandomToDoId(), generateRandomToDoId());

        var result = toDoRelationshipsCommandUseCases.assignToDoParent(command);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    @Test
    public void should_ThrowException_When_AssigningDescendentToDo_As_Parent_For_AncestorToDo()
    {
        var firstId = toDoCreationUtilService.createToDo(generateRandomToDoName()).getId();
        var secondId = toDoCreationUtilService.createToDo(generateRandomToDoName()).getId();
        var thirdId = toDoCreationUtilService.createToDo(generateRandomToDoName()).getId();

        var result =
            toDoRelationshipsCommandUseCases
                    .assignToDoParent(new AssignToDoParentCommand(secondId, firstId))
                    .then(
                            Mono.defer(
                                    () ->
                                        toDoRelationshipsCommandUseCases.assignToDoParent(
                                                new AssignToDoParentCommand(thirdId, secondId)
                                        )
                            )
                    )
                    .then(
                            Mono.defer(
                                    () ->
                                        toDoRelationshipsCommandUseCases.assignToDoParent(
                                                new AssignToDoParentCommand(firstId, thirdId)
                                        )
                            )
                    );

        StepVerifier
                .create(result)
                .expectError(ToDoIsInCorrectToBeParentException.class)
                .verify();
    }
}
