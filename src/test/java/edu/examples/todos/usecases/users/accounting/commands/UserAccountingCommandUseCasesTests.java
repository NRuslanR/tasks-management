package edu.examples.todos.usecases.users.accounting.commands;

import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.commands.create.IncorrectCreateUserCommandException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.StringUtils;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class UserAccountingCommandUseCasesTests
{
    private final UserAccountingCommandUseCases userAccountingCommandUseCases;

    @ParameterizedTest
    @MethodSource("createCommandsForUserCreating")
    public void should_Return_CreatedUser_When_CommandIsCorrect_And_UserDoesNotExistsYet(CreateUserCommand command)
    {
        var result = userAccountingCommandUseCases.createUser(command);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var user = v.getUser();

                    assertTrue(StringUtils.hasText(user.getId()));
                    assertNotNull(user.getCreatedAt());
                    assertEquals(command.getFirstName(), user.getFirstName());
                    assertEquals(command.getLastName(), user.getLastName());

                    var allowedToDoCreationCount = command.getAllowedToDoCreationCount();

                    if (!Objects.isNull(allowedToDoCreationCount))
                        assertEquals(allowedToDoCreationCount, user.getAllowedToDoCreationCount());

                    var canPerformForeignTodos = command.getPerformForeignTodosAllowed();

                    if (!Objects.isNull(canPerformForeignTodos))
                        assertEquals(canPerformForeignTodos, user.getPerformForeignTodosAllowed());

                    var canEditForeignTodos = command.getEditForeignTodosAllowed();

                    if (!Objects.isNull(canEditForeignTodos))
                        assertEquals(canEditForeignTodos, user.getEditForeignTodosAllowed());

                    var canRemoveForeignTodos = command.getRemoveForeignTodosAllowed();

                    if (!Objects.isNull(canRemoveForeignTodos))
                        assertEquals(canRemoveForeignTodos, user.getRemoveForeignTodosAllowed());
                })
                .verifyComplete();
    }

    public  Stream<Arguments> createCommandsForUserCreating()
    {
        return Stream.of(
                Arguments.of(UserAccountingCommandUseCasesTestsUtils.createSimpleCommandForRandomUserCreating()),
                Arguments.of(UserAccountingCommandUseCasesTestsUtils.createCommandForRandomUserCreatingWithRandomClaims()),
                Arguments.of(new CreateUserCommand("a", "b", 434, true, true, true))
        );
    }

    @ParameterizedTest
    @MethodSource("createIncorrectCreateUserCommands")
    public void should_ThrowException_When_CreateUserCommand_Is_InCorrect(CreateUserCommand incorrectCommand)
    {
        var result = userAccountingCommandUseCases.createUser(incorrectCommand);

        StepVerifier
                .create(result)
                .expectError(IncorrectCreateUserCommandException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectCreateUserCommands()
    {
        return Stream.of(
                Arguments.of(UserAccountingCommandUseCasesTestsUtils.createIncorrectBlankCommandForUserCreating()),
                Arguments.of(new CreateUserCommand("a", "")),
                Arguments.of(new CreateUserCommand("", "b"))
        );
    }
}
