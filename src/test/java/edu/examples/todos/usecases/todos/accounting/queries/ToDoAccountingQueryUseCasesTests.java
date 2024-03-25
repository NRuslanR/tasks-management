package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.IncorrectGetByIdQueryException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
                .createSimpleCommandsForToDoCreating("ToDo#1", "ToDO#2")
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

    private GetByIdQuery createFindToDoByIdQuery(String toDoId)
    {
        return ToDoAccountingQueryUseCasesTestsUtils.createGetByIdQuery(toDoId);
    }

    @Test
    public void should_ThrowException_When_GetByIdQuery_IsNotCorrect()
    {
        var incorrectQuery = ToDoAccountingQueryUseCasesTestsUtils.createIncorrectGetByIdQuery();

        var result = toDoAccountingQueryUseCases.getToDoById(incorrectQuery);

        StepVerifier
            .create(result)
            .expectError(IncorrectGetByIdQueryException.class)
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

    @Test
    public void should_Return_AllToDos_When_QueryIsValid()
    {
        var query = ToDoAccountingQueryUseCasesTestsUtils.createQueryToFindAllToDos();

        var result = toDoAccountingQueryUseCases.findToDos(query);

        StepVerifier
                .create(result)
                .assertNext(v -> {

                    assertNotNull(v);

                    var toDoPage = v.getToDoPage();

                    assertNotNull(toDoPage);

                    var actualToDos = toDoPage.getContent();

                    assertFalse(actualToDos.isEmpty());
                    assertThat(actualToDos, containsInAnyOrder(toDos.toArray()));
                })
                .verifyComplete();
    }

    @Test
    public void should_Return_ValidToDos()
    {

    }
}
