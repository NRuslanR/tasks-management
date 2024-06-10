package edu.examples.todos.usecases.users.accounting.queries;

import edu.examples.todos.usecases.users.accounting.UserDto;
import edu.examples.todos.usecases.users.accounting.common.exceptions.UserNotFoundException;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdQuery;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.GetUserByIdResult;
import edu.examples.todos.usecases.users.accounting.queries.getbyid.IncorrectGetUserByIdQueryException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class JdbcUserAccountingQueryUseCases implements UserAccountingQueryUseCases
{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mono<GetUserByIdResult> getUserById(GetUserByIdQuery query)
    {
        return
                ensureGetByIdQueryIsValid(query)
                        .flatMap(this::doGetUserById);
    }

    private Mono<GetUserByIdQuery> ensureGetByIdQueryIsValid(GetUserByIdQuery query)
    {
        return Mono.fromCallable(() -> {

            Objects.requireNonNull(query);

            if (StringUtils.hasText(query.getUserId()))
                return query;

            throw new IncorrectGetUserByIdQueryException();
        });
    }

    private Mono<GetUserByIdResult> doGetUserById(GetUserByIdQuery query)
    {
        return
                Mono
                    .fromCallable(
                        () ->
                            jdbcTemplate.queryForObject(
                                "SELECT " +
                                    "*," +
                                    "canEditForeignTodos AS editForeignTodosAllowed," +
                                    "canRemoveForeignTodos AS removeForeignTodosAllowed," +
                                    "canPerformForeignTodos AS performForeignTodosAllowed " +
                                    "FROM users WHERE id = ?",
                                new DataClassRowMapper<>(UserDto.class),
                                query.getUserId()
                            )
                    )
                    .onErrorResume(
                            DataIntegrityViolationException.class,
                            e -> Mono.error(new IncorrectGetUserByIdQueryException())
                    )
                    .onErrorResume(
                        EmptyResultDataAccessException.class,
                        e -> Mono.error(new UserNotFoundException())
                    )
                    .map(GetUserByIdResult::new);
    }
}
