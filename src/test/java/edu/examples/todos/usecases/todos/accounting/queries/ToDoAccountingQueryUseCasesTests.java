package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.IncorrectFindToDosQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.IncorrectGetToDoByIdQueryException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ToDoAccountingQueryUseCasesTests
{
    private final ToDoAccountingQueryUseCases toDoAccountingQueryUseCases;

    private List<ToDoDto> toDos;

    @BeforeAll
    public void setupFixtureForAll(@Autowired ToDoAccountingCommandUseCases toDoAccountingCommandUseCases)
    {
        toDos =
            ToDoAccountingCommandUseCasesTestsUtils
                .createSimpleCommandsForToDoCreating("ToDo#1", "ToDO#2", "ToDo#3")
                    .stream()
                    .map(toDoAccountingCommandUseCases::createToDo)
                    .map(Mono::block)
                    .map(CreateToDoResult::getToDo)
                    .toList();
    }

    @Test
    public void should_Return_ToDoById_When_QueryIsValid_And_ToDoExists()
    {
        var expectedToDo = toDos.get(0);

        var query = createFindToDoByIdQuery(expectedToDo.getId());

        var result = toDoAccountingQueryUseCases.getToDoById(query);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var actualToDo = v.getToDo();

                    assertNotNull(actualToDo);

                    assertTrue(StringUtils.doesNotContainWhitespace(actualToDo.getId()));

                    assertEquals(expectedToDo.getId(), actualToDo.getId());
                    assertEquals(expectedToDo.getName(), actualToDo.getName());
                    assertEquals(expectedToDo.getDescription(), actualToDo.getDescription());
                    assertEquals(expectedToDo.getCreatedAt(), actualToDo.getCreatedAt());
                })
                .verifyComplete();
    }

    private GetToDoByIdQuery createFindToDoByIdQuery(String toDoId)
    {
        return ToDoAccountingQueryUseCasesTestsUtils.createGetByIdQuery(toDoId);
    }

    @Test
    public void should_ThrowException_When_GetByIdQuery_IsInCorrect()
    {
        var incorrectQuery = ToDoAccountingQueryUseCasesTestsUtils.createIncorrectGetByIdQuery();

        var result = toDoAccountingQueryUseCases.getToDoById(incorrectQuery);

        StepVerifier
            .create(result)
            .expectError(IncorrectGetToDoByIdQueryException.class)
            .verify();
    }

    @Test
    public void should_ThrowException_When_ToDoNotFound_By_Id()
    {
        var query = ToDoAccountingQueryUseCasesTestsUtils.createGetByIdQuery(UUID.randomUUID().toString());

        var result = toDoAccountingQueryUseCases.getToDoById(query);

        StepVerifier
            .create(result)
            .expectError(ToDoNotFoundException.class)
            .verify();
    }

    @ParameterizedTest
    @MethodSource("createFindToDosQueries")
    public void should_Return_ValidToDos(FindToDosQuery query)
    {
        var result = toDoAccountingQueryUseCases.findToDos(query);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var toDoPage = v.getToDoPage();

                    assertNotNull(toDoPage);

                    var pageQuery = query.getPageQuery();
                    var actualPageable = toDoPage.getPageable();
                    var actualToDos = toDoPage.getContent();

                    assertNotNull(actualToDos);

                    if (pageQuery.isPaged())
                    {
                        assertEquals(pageQuery, actualPageable);
                        assertTrue(pageQuery.getPageSize() >= actualToDos.size());
                    }
                })
                .verifyComplete();
    }

    public Stream<Arguments> createFindToDosQueries()
    {
            return Stream.of(
                    Arguments.of(
                            ToDoAccountingQueryUseCasesTestsUtils.createQueryToFindAllToDos()
                    ),
                    Arguments.of(
                            ToDoAccountingQueryUseCasesTestsUtils.createQueryToFindToDoPage(1)
                    ),
                    Arguments.of(
                            ToDoAccountingQueryUseCasesTestsUtils.createQueryToFindToDoPage(1, 1)
                    ),
                    Arguments.of(
                            ToDoAccountingQueryUseCasesTestsUtils.createQueryToFindToDoPage(toDos.size(), 1)
                    )
            );
    }

    @ParameterizedTest
    @MethodSource("createIncorrectFindToDosQueries")
    public void should_ThrowException_When_FindToDosQuery_IsInCorrect(FindToDosQuery incorrectQuery)
    {
        var result = toDoAccountingQueryUseCases.findToDos(incorrectQuery);

        StepVerifier
                .create(result)
                .expectError(IncorrectFindToDosQueryException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectFindToDosQueries()
    {
        return Stream.of(
                Arguments.of(
                        ToDoAccountingQueryUseCasesTestsUtils.createFindToDosQueryWithIncorrectFilter()
                )
        );
    }
}
