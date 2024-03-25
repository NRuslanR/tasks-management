package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.accounting.queries.common.FilterQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.GetByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.findbyid.IncorrectGetByIdQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosResult;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.IncorrectFindToDosQueryException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

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
                ensureGetByIdQueryIsValid(query)
                        .flatMap(this::doGetToDoById);
    }

    private Mono<GetByIdQuery> ensureGetByIdQueryIsValid(GetByIdQuery query) throws IncorrectGetByIdQueryException
    {
        return StringUtils.hasText(query.getToDoId()) ? Mono.just(query) : Mono.error(new IncorrectGetByIdQueryException());
    }

    private Mono<GetByIdResult> doGetToDoById(GetByIdQuery findByIdQuery)
            throws IncorrectGetByIdQueryException, ToDoNotFoundException
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

    @Override
    public Mono<FindToDosResult> findToDos(@NonNull FindToDosQuery query)
            throws NullPointerException, IncorrectFindToDosQueryException
    {
        return
                ensureFindToDosQueryIsValid(query)
                        .flatMap(this::doFindToDos);
    }

    private Mono<FindToDosQuery> ensureFindToDosQueryIsValid(FindToDosQuery query)
    {
        return
                !query.getFilterQuery().isEmpty() &&
                !query
                    .getFilterQuery()
                    .getFields()
                    .keySet()
                    .stream()
                    .allMatch(StringUtils::hasText) ?
                        Mono.error(new IncorrectFindToDosQueryException()) : Mono.just(query);
    }

    private Mono<FindToDosResult> doFindToDos(FindToDosQuery query)
    {
        /*
        refactor:
            extract whereClause, orderClause, pagingClause, whereFieldValues
            as separate object and inject it to this
        * */
        return
                Mono.fromCallable(() ->

                    jdbcTemplate.query(
                        String.format(
                                "SELECT * FROM todos %s %s %s",
                                whereClause(query.getFilterQuery()),
                                orderClause(query.getPageQuery().getSort()),
                                pagingClause(query.getPageQuery())
                        ),
                        new DataClassRowMapper<>(ToDoDto.class),
                        whereFieldValues(query.getFilterQuery())
                    )
                )
                .flatMap(
                        v -> Mono.just(
                            new Pair<List<ToDoDto>, Long>(
                                    v,
                                    jdbcTemplate.queryForObject("SELECT count(*) FROM todos", Long.class)
                            )
                        )
                )
                .map(v ->
                        new PageImpl<ToDoDto>(
                            v.getValue0(),
                            query.getPageQuery(),
                            v.getValue1()
                        )
                )
                .map(FindToDosResult::new);
    }

    private String whereClause(FilterQuery filterQuery)
    {
        return
            !filterQuery.isEmpty() ?
                "WHERE " +
                StringUtils.collectionToDelimitedString(
                    filterQuery.getFields().keySet().stream().map(f -> f + "=?").toList(),
                    " AND "
                ) : "";
    }

    private Object[] whereFieldValues(FilterQuery filterQuery)
    {
        return filterQuery.getFields().values().toArray();
    }

    private String orderClause(Sort sort)
    {
        return
            sort.isSorted() ?
                "ORDER BY " +
                StringUtils.collectionToCommaDelimitedString(
                    sort.get().map(o -> o.getProperty() + " " + o.getDirection()).toList()
                ) : "";
    }

    private String pagingClause(Pageable pageQuery)
    {
        /* refactor: LIMIT is non-universal sql clause, use JOOQ to resolve that */
        return
            pageQuery.isPaged()  ? "LIMIT " + pageQuery.getPageSize() + "OFFSET " + pageQuery.getOffset() : "";
    }
}
