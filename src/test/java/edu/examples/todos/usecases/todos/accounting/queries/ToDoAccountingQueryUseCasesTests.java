package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCases;
import edu.examples.todos.usecases.todos.accounting.commands.ToDoAccountingCommandUseCasesTestsUtils;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoResult;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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
                    assertEquals(expectedToDo.getCreatedAt().withNano(0), actualToDo.getCreatedAt().withNano(0));
                })
                .verifyComplete();
    }

    private GetByIdQuery createFindToDoByIdQuery(String toDoId)
    {
        return ToDoAccountingQueryUseCasesTestsUtils.createFindByIdQuery(toDoId);
    }
}
