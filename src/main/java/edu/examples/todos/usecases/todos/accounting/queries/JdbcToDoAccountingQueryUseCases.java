package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.IncorrectGetByIdQueryException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class JdbcToDoAccountingQueryUseCases implements ToDoAccountingQueryUseCases
{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mono<GetByIdResult> getToDoById(@NonNull GetByIdQuery query)
            throws NullPointerException, IncorrectGetByIdQueryException, ToDoNotFoundException
    {
        return
                ensureQueryIsValid(query)
                        .flatMap(this::doGetToDoById);
    }

    private Mono<GetByIdQuery> ensureQueryIsValid(GetByIdQuery query) throws IncorrectGetByIdQueryException
    {
        return StringUtils.hasText(query.getToDoId()) ? Mono.just(query) : Mono.error(new IncorrectGetByIdQueryException());
    }

    private Mono<GetByIdResult> doGetToDoById(GetByIdQuery findByIdQuery) throws IncorrectGetByIdQueryException, ToDoNotFoundException
    {
        /* refactor: use JOOQ instead */
        return
               Mono.fromCallable(() ->

                   jdbcTemplate.queryForObject(
                           "SELECT * FROM todos WHERE id = ?",
                           new DataClassRowMapper<>(ToDoDto.class),
                           findByIdQuery.getToDoId()
                   )
               )
               .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new IncorrectGetByIdQueryException()))
               .onErrorResume(EmptyResultDataAccessException.class, e -> Mono.error(new ToDoNotFoundException()))
               .map(GetByIdResult::new);
    }
}
