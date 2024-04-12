package edu.examples.todos.usecases.todos.accounting.queries;

import edu.examples.todos.common.util.DataTransforms;
import edu.examples.todos.common.util.HierarchicalItem;
import edu.examples.todos.usecases.common.accounting.queries.FilterQuery;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.ToDoFullInfoDto;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosQuery;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.FindToDosResult;
import edu.examples.todos.usecases.todos.accounting.queries.findtodos.IncorrectFindToDosQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.GetToDoByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.getbyid.IncorrectGetToDoByIdQueryException;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdQuery;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.GetToDoFullInfoByIdResult;
import edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid.IncorrectGetToDoFullInfoByIdQueryException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
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

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Objects;

/*
    CQRS-based use-cases service implementation
 */
/*
    refactor: JOOQ use and extract throws list in doc-comment due to Monos
 */
@Service
@RequiredArgsConstructor
public class JdbcToDoAccountingQueryUseCases implements ToDoAccountingQueryUseCases
{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mono<GetToDoByIdResult> getToDoById(GetToDoByIdQuery query)
            throws NullPointerException, IncorrectGetToDoByIdQueryException, ToDoNotFoundException
    {
        return
                ensureGetByIdQueryIsValid(query)
                        .flatMap(this::doGetToDoById);
    }

    private Mono<GetToDoByIdQuery> ensureGetByIdQueryIsValid(GetToDoByIdQuery query)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(query))
                    .flatMap(v ->
                            StringUtils.hasText(query.getToDoId()) ? Mono.just(query) :
                                    Mono.error(new IncorrectGetToDoByIdQueryException())
                    );
    }

    private Mono<GetToDoByIdResult> doGetToDoById(GetToDoByIdQuery findByIdQuery)
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
               .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new IncorrectGetToDoByIdQueryException()))
               .onErrorResume(EmptyResultDataAccessException.class, e -> Mono.error(new ToDoNotFoundException()))
               .map(GetToDoByIdResult::new);
    }

    @Override
    public Mono<FindToDosResult> findToDos(FindToDosQuery query)
    {
        return
                ensureFindToDosQueryIsValid(query)
                        .flatMap(this::doFindToDos);
    }

    private Mono<FindToDosQuery> ensureFindToDosQueryIsValid(FindToDosQuery query)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(query))
                    .flatMap(v ->
                        !query.getFilterQuery().isEmpty() &&
                        !query
                            .getFilterQuery()
                            .getFields()
                            .keySet()
                            .stream()
                            .allMatch(StringUtils::hasText) ?
                                Mono.error(new IncorrectFindToDosQueryException()) : Mono.just(query)
                    );
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
                .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new IncorrectFindToDosQueryException()))
                .onErrorResume(SQLSyntaxErrorException.class, e -> Mono.error(new IncorrectFindToDosQueryException()))
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

    @Override
    public Mono<GetToDoFullInfoByIdResult> getToDoFullInfoById(GetToDoFullInfoByIdQuery query)
            throws NullPointerException, IncorrectGetToDoFullInfoByIdQueryException, ToDoNotFoundException
    {
        return
                ensureGetToDoFullInfoByIdQueryIsValid(query)
                        .flatMap(this::doGetToDoFullInfoById);
    }

    private Mono<GetToDoFullInfoByIdQuery> ensureGetToDoFullInfoByIdQueryIsValid(GetToDoFullInfoByIdQuery query)
    {
        return
                Mono
                    .fromCallable(() -> Objects.requireNonNull(query))
                    .map(v -> {

                        if (StringUtils.hasText(query.getToDoId()))
                            return v;

                        throw new IncorrectGetToDoFullInfoByIdQueryException();
                    });
    }

    private Mono<GetToDoFullInfoByIdResult> doGetToDoFullInfoById(GetToDoFullInfoByIdQuery getToDoFullInfoById)
    {
        return
                getAllToDosRecursivelyStartingWith(getToDoFullInfoById.getToDoId())
                        .map(this::toGetToDoFullInfoByIdResult);
    }

    private Mono<List<ToDoDto>> getAllToDosRecursivelyStartingWith(String toDoId)
    {
        return
                Mono.fromCallable(
                        () ->
                                jdbcTemplate.query(
                                        "WITH RECURSIVE get_all_sub_todos(id) AS (" +
                                                "SELECT id FROM todos WHERE id = ? " +
                                                "UNION " +
                                                "SELECT t.id FROM todos t " +
                                                "JOIN get_all_sub_todos st ON t.parentToDoId = st.id " +
                                                ") SELECT t.* FROM get_all_sub_todos st " +
                                                "JOIN todos t ON st.id = t.id",
                                        new DataClassRowMapper<>(ToDoDto.class),
                                        toDoId
                                )
                )
                .onErrorResume(
                        DataIntegrityViolationException.class,
                        e -> Mono.error(new IncorrectGetToDoFullInfoByIdQueryException())
                )
                .flatMap(
                    v -> !v.isEmpty() ? Mono.just(v) : Mono.error(new ToDoNotFoundException())
                );
    }

    private GetToDoFullInfoByIdResult toGetToDoFullInfoByIdResult(List<ToDoDto> toDoDtos)
    {
        return new GetToDoFullInfoByIdResult(
            DataTransforms
                .transformFlatDataToHierarchical(
                        toDoDtos,
                        "parentToDoId",
                        "id"
                )
                .stream()
                .map(this::toToDoFullInfoDto)
                .findFirst()
                .get()
        );
    }

    private ToDoFullInfoDto toToDoFullInfoDto(HierarchicalItem<ToDoDto> item)
    {
        return new ToDoFullInfoDto(
                item.getItem(),
                item.getSubItems().stream().map(this::toToDoFullInfoDto).toList()
        );
    }
}
