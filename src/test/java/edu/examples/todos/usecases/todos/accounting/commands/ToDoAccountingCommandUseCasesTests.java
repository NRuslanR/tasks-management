package edu.examples.todos.usecases.todos.accounting.commands;

import edu.examples.todos.usecases.todos.accounting.ToDoAlreadyExistsException;
import edu.examples.todos.usecases.todos.accounting.commands.create.IncorrectCreateToDoCommandException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils.createSimpleCommandForToDoCreating;
import static edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils.createSimpleIncorrectCommandForToDoCreating;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ToDoAccountingCommandUseCasesTests
{
    protected final ToDoAccountingCommandUseCases toDoAccountingUseCases;

    private enum ToDoNameUseCases { SUCCESSFUL, ALREADY_EXISTS }

    private Map<ToDoNameUseCases, String> testToDoNames =
            Map.of(
                    ToDoNameUseCases.SUCCESSFUL, "To-Do#1",
                    ToDoNameUseCases.ALREADY_EXISTS, "To-Do#2"
            );

    @Test
    public void should_Create_ToDo_When_CreateToDoCommand_IsCorrect_And_ToDoDoesNotExistsYet()
    {
        var toDoName = testToDoNames.get(ToDoNameUseCases.SUCCESSFUL);

        var createToDoCommand = createSimpleCommandForToDoCreating(toDoName);

        var createToDoResult = toDoAccountingUseCases.createToDo(createToDoCommand);

        StepVerifier
            .create(createToDoResult)
            .assertNext(v -> {

                assertNotNull(v);

                var toDoDto = v.getToDo();

                assertNotNull(toDoDto);
                assertTrue(StringUtils.doesNotContainWhitespace(toDoDto.getId()));
                assertEquals(createToDoCommand.getName(), toDoDto.getName());
                assertEquals(createToDoCommand.getDescription(), toDoDto.getDescription());
                assertNotNull(toDoDto.getCreatedAt());
            })
            .verifyComplete();
    }

    @Test
    public void should_ThrowException_When_CreateToDoCommand_IsNotCorrect()
    {
        var incorrectCreateToDoCommand = createSimpleIncorrectCommandForToDoCreating();

        var createToDoResult = toDoAccountingUseCases.createToDo(incorrectCreateToDoCommand);

        StepVerifier
            .create(createToDoResult)
            .expectError(IncorrectCreateToDoCommandException.class)
            .verify();
    }

    @Test
    public void should_ThrowException_When_ToDoAlreadyExists()
    {
        var toDoName = testToDoNames.get(ToDoNameUseCases.ALREADY_EXISTS);

        var createToDoCommand = createSimpleCommandForToDoCreating(toDoName);
        var errorCreateToDoCommand = createSimpleCommandForToDoCreating(toDoName);


        var createToDoResult =
                toDoAccountingUseCases.createToDo(createToDoCommand)
                        .then(
                                Mono.defer(
                                        () -> toDoAccountingUseCases.createToDo(errorCreateToDoCommand)
                                )
                        );
        StepVerifier
                .create(createToDoResult)
                .expectError(ToDoAlreadyExistsException.class)
                .verify();
    }
}
