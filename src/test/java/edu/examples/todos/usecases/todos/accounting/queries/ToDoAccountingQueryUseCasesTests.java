package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.IncorrectFindToDosQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.IncorrectGetToDoByIdQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.IncorrectGetToDoFullInfoByIdQueryException;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.relationships.commands.ToDoRelationshipsCommandUseCases;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoId;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ToDoAccountingQueryUseCasesTests
{
    private final ToDoAccountingQueryUseCases toDoAccountingQueryUseCases;

    @Autowired
    private ToDoCreationUtilService toDoCreationUtilService;

    @Autowired
    private ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases;

    private List<ToDoDto> toDos;

    @BeforeAll
    public void setupFixtureForAll()
    {
        toDos = toDoCreationUtilService.createRandomToDos();
    }

    @AfterAll
    public void clearFixtureForAll()
    {

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

                    assertToDosEquals(expectedToDo, actualToDo);
                })
                .verifyComplete();
    }

    private GetToDoByIdQuery createFindToDoByIdQuery(String toDoId)
    {
        return ToDoAccountingQueryUseCasesTestsUtils.createGetToDoByIdQuery(toDoId);
    }

    @Test
    public void should_ThrowException_When_GetByIdQuery_IsInCorrect()
    {
        var incorrectQuery = ToDoAccountingQueryUseCasesTestsUtils.createIncorrectGetToDoByIdQuery();

        var result = toDoAccountingQueryUseCases.getToDoById(incorrectQuery);

        StepVerifier
            .create(result)
            .expectError(IncorrectGetToDoByIdQueryException.class)
            .verify();
    }

    @Test
    public void should_ThrowException_When_ToDoNotFound_By_Id()
    {
        var query =
                ToDoAccountingQueryUseCasesTestsUtils
                        .createGetToDoByIdQuery(generateRandomToDoId());

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

                    /*
                        refactor: implement RemoveAllToDosCommand to remove all to-dos before query use-case tests

                        assertTrue(toDos.containsAll(actualToDos));
                     */

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

    @Test
    public void should_Return_ToDoFullInfo_When_QueryIsValid_And_ToDoExists()
    {
        var toDos = createRandomToDoBranch();

        ToDoDto root = toDos.get(0), toDo_1 = toDos.get(1), toDo_2 = toDos.get(2), toDo_11 = toDos.get(3);

        var query = ToDoAccountingQueryUseCasesTestsUtils.createGetToDoFullInfoByIdQuery(root.getId());

        var result = toDoAccountingQueryUseCases.getToDoFullInfoById(query);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var toDoFullInfo = v.getToDoFullInfo();

                    assertNotNull(toDoFullInfo);

                    assertToDosEquals(root, toDoFullInfo.getToDo());

                    var subToDos = toDoFullInfo.getSubToDos();

                    assertNotNull(subToDos);

                    assertEquals(2, subToDos.size());

                    var actualToDo_1 = toDoFullInfo.findSubToDoById(toDo_1.getId());
                    var actualToDo_2 = toDoFullInfo.findSubToDoById(toDo_2.getId());

                    assertTrue(
                            BooleanUtils.and(new boolean[] {
                                    actualToDo_1.isPresent(),
                                    actualToDo_2.isPresent()
                            })
                    );

                    assertToDosEquals(toDo_1, actualToDo_1.get().getToDo());
                    assertToDosEquals(toDo_2, actualToDo_2.get().getToDo());

                    subToDos = actualToDo_1.get().getSubToDos();

                    assertNotNull(subToDos);

                    assertEquals(1, subToDos.size());

                    assertToDosEquals(subToDos.get(0).getToDo(), toDo_11);
                })
                .verifyComplete();
    }

    private List<ToDoDto> createRandomToDoBranch()
    {
        var toDos = toDoCreationUtilService.createRandomToDos(4);

        return
                List.of(
                    toDos.get(0),
                    toDoRelationshipsCommandUseCases.assignToDoParent(
                            new AssignToDoParentCommand(toDos.get(1).getId(), toDos.get(0).getId())
                    ).block().getToDo(),
                    toDoRelationshipsCommandUseCases.assignToDoParent(
                            new AssignToDoParentCommand(toDos.get(2).getId(), toDos.get(0).getId())
                    ).block().getToDo(),
                    toDoRelationshipsCommandUseCases.assignToDoParent(
                            new AssignToDoParentCommand(toDos.get(3).getId(), toDos.get(1).getId())
                    ).block().getToDo()
                );
    }

    @ParameterizedTest
    @MethodSource("createIncorrectGetToDoFullInfoByIdQuery")
    public void should_ThrowException_When_GetToDoFullInfoByIdQuery_IsInCorrect(GetToDoFullInfoByIdQuery incorrectQuery)
    {
        var result = toDoAccountingQueryUseCases.getToDoFullInfoById(incorrectQuery);

        StepVerifier
                .create(result)
                .expectError(IncorrectGetToDoFullInfoByIdQueryException.class)
                .verify();
    }

    public Stream<Arguments> createIncorrectGetToDoFullInfoByIdQuery()
    {
        return Stream.of(
                Arguments.of(
                    ToDoAccountingQueryUseCasesTestsUtils
                            .createIncorrectGetToDoFullInfoByIdQuery()
                )
        );
    }

    @Test
    public void should_ThrowException_When_ToDoFullInfoNotFound_By_Id()
    {
        var query =
                ToDoAccountingQueryUseCasesTestsUtils
                        .createGetToDoFullInfoByIdQuery(generateRandomToDoId());

        var result = toDoAccountingQueryUseCases.getToDoFullInfoById(query);

        StepVerifier
                .create(result)
                .expectError(ToDoNotFoundException.class)
                .verify();
    }

    private void assertToDosEquals(ToDoDto expectedToDo, ToDoDto actualToDo)
    {
        assertEquals(expectedToDo.getId(), actualToDo.getId());
        assertEquals(expectedToDo.getName(), actualToDo.getName());
        assertEquals(expectedToDo.getDescription(), actualToDo.getDescription());
        assertEquals(expectedToDo.getCreatedAt(), actualToDo.getCreatedAt());
        assertEquals(expectedToDo.getParentToDoId(), actualToDo.getParentToDoId());
        assertEquals(expectedToDo.getActionsAvailability(), actualToDo.getActionsAvailability());
    }
}
