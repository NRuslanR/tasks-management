package edu.examples.todos.usecases.users.accounting.queries;

import edu.examples.todos.usecases.users.accounting.UserDto;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCases;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCasesTestsUtils;
import edu.examples.todos.usecases.users.accounting.common.exceptions.UserNotFoundException;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdQuery;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.IncorrectGetUserByIdQueryException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class UserAccountingQueryUseCasesTests
{
    private final UserAccountingQueryUseCases userAccountingQueryUseCases;

    private final UserAccountingCommandUseCases userAccountingCommandUseCases;

    @Test
    public void should_Return_UserById_When_QueryIsValid_And_UserExists()
    {
        var expectedUser = createRandomUser();

        var query = new GetUserByIdQuery(expectedUser.getId());

        var result = userAccountingQueryUseCases.getUserById(query);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var actualUser = v.getUser();

                    assertNotNull(actualUser);

                    assertUsersEquals(expectedUser, actualUser);
                })
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("createIncorrectGetUserByIdQueries")
    public void should_ThrowException_When_GetUserByIdQuery_Is_InCorrect(GetUserByIdQuery incorrectQuery)
    {
        var result = userAccountingQueryUseCases.getUserById(incorrectQuery);

        StepVerifier
                .create(result)
                .expectError(IncorrectGetUserByIdQueryException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectGetUserByIdQueries()
    {
        return
                UserAccountingQueryUseCasesTestsUtils
                        .createIncorrectQueriesForGettingUserById()
                        .stream()
                        .map(Arguments::of);
    }

    @Test
    public void should_ThrowException_When_UserNotFound_By_Id()
    {
        var query = UserAccountingQueryUseCasesTestsUtils.createQueryForUserNotFoundById();

        var result = userAccountingQueryUseCases.getUserById(query);

        StepVerifier
                .create(result)
                .expectError(UserNotFoundException.class)
                .verify();
    }

    private UserDto createRandomUser()
    {
        return
                userAccountingCommandUseCases
                    .createUser(
                        UserAccountingCommandUseCasesTestsUtils
                            .createSimpleCommandForRandomUserCreating()
                    )
                    .block()
                    .getUser();
    }

    private void assertUsersEquals(UserDto expectedUser, UserDto actualUser)
    {
        assertEquals(expectedUser, actualUser);
    }
}
