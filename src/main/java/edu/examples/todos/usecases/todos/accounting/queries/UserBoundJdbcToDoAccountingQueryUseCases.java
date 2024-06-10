package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.domain.actors.todos.ToDoActionIsNotAvailableException;
import edu.examples.todos.usecases.common.annotations.ConditionalOnUserBinding;
import edu.examples.todos.usecases.todos.accounting.queries.common.JdbcToDoDtoMapper;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.IncorrectGetToDoByIdQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.IncorrectGetToDoFullInfoByIdQueryException;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.users.accounting.services.CurrentUserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@ConditionalOnUserBinding
@Service
public class UserBoundJdbcToDoAccountingQueryUseCases extends JdbcToDoAccountingQueryUseCases
{
    private final CurrentUserService currentUserService;

    public UserBoundJdbcToDoAccountingQueryUseCases(CurrentUserService currentUserService, JdbcTemplate jdbcTemplate, JdbcToDoDtoMapper toDoDtoMapper)
    {
        super(jdbcTemplate, toDoDtoMapper);

        this.currentUserService = currentUserService;
    }

    @Override
    protected String joins(String targetTableAlias)
    {
        var userId = currentUserService.getCurrentUserId().getValue().toString();

        return String.format(
                "JOIN users u ON u.id = %s.authorId " +
                "LEFT JOIN users requester ON requester.id = '" + userId + "'",
                targetTableAlias
        );
    }

    @Override
    protected String toDoActionsAvailabilitySelection()
    {
        var userId = currentUserService.getCurrentUserId().getValue().toString();

        return String.format(
            "u.id='" + userId + "' OR requester.canEditForeignTodos OR requester.canRemoveForeignTodos OR requester.canPerformForeignTodos as viewingAvailable," +
            "(u.id='" + userId + "' OR requester.canEditForeignTodos) AND performedAt is null as changingAvailable," +
            "(u.id='" + userId + "' OR requester.canRemoveForeignTodos) as removingAvailable," +
            "(u.id='" + userId + "' OR requester.canEditForeignTodos) AND performedAt is null as parentAssigningAvailable," +
            "(u.id='" + userId + "' OR requester.canPerformForeignTodos) AND performedAt is null as performingAvailable"
        );
    }

    @Override
    public Mono<GetToDoByIdResult> getToDoById(GetToDoByIdQuery query)
            throws NullPointerException, IncorrectGetToDoByIdQueryException, ToDoNotFoundException
    {
        return
                super
                    .getToDoById(query)
                        .map(result -> {

                            ensureToDoViewingAvailable(result.getToDo());

                            return result;
                        });
    }

    @Override
    public Mono<GetToDoFullInfoByIdResult> getToDoFullInfoById(GetToDoFullInfoByIdQuery query)
            throws NullPointerException, IncorrectGetToDoFullInfoByIdQueryException, ToDoNotFoundException
    {
        return
                super
                    .getToDoFullInfoById(query)
                        .map(result -> {

                            ensureToDoViewingAvailable(result.getToDoFullInfo().getToDo());

                            return result;
                        });
    }

    private ToDoDto ensureToDoViewingAvailable(ToDoDto toDo)
    {
        if (!toDo.getActionsAvailability().isViewingAvailable())
        {
            throw new ToDoActionIsNotAvailableException("To-Do's viewing isn't available");
        }

        return toDo;
    }

    @Override
    protected String findToDosPostWhere()
    {
        return "WHERE viewingAvailable";
    }
}
