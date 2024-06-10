package edu.examples.todos.usecases.common.accounting.queries;

import edu.examples.todos.usecases.common.exceptions.UseCasesException;
import edu.examples.todos.usecases.common.extensions.UnpagedSorted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindObjectsQuery
{
    @NonNull
    private Pageable pageQuery;

    @NonNull
    private FilterQuery filterQuery;

    public static FindObjectsQuery allObjects()
    {
        return FindObjectsQuery.of(Pageable.unpaged(), FilterQuery.empty());
    }

    public static FindObjectsQuery ofPage(Pageable pageQuery)
    {
        return FindObjectsQuery.of(pageQuery, FilterQuery.empty());
    }

    public static FindObjectsQuery filteredObjects(FilterQuery filterQuery)
    {
        return FindObjectsQuery.of(Pageable.unpaged(), filterQuery);
    }

    public static FindObjectsQuery of(Pageable pageQuery, FilterQuery filterQuery)
    {
        return new FindObjectsQuery(pageQuery, filterQuery);
    }

    public static Builder builder()
    {
        return builder;
    }

    private static Builder builder = new Builder();

    public static class Builder
    {
        private Pageable pageQuery;
        private FilterQuery filterQuery;

        public Builder withPagination(
                Optional<Integer> page,
                Optional<Integer> size,
                Optional<List<String>> sort
        )
        {
            return withPagination(page, 0, size, Integer.MAX_VALUE, sort);
        }

        public FindObjectsQuery build()
        {
            var query =
                !Objects.isNull(pageQuery) && !Objects.isNull(filterQuery) ? FindObjectsQuery.of(pageQuery, filterQuery) :
                    !Objects.isNull(pageQuery) ? FindObjectsQuery.ofPage(pageQuery):
                    !Objects.isNull(filterQuery) ? FindObjectsQuery.filteredObjects(filterQuery)  :
                            FindObjectsQuery.allObjects();

            pageQuery = null;
            filterQuery = null;

            return query;
        }

        public Builder withPagination(
                Optional<Integer> page,
                int defaultPage,
                Optional<Integer> size,
                int defaultPageSize,
                Optional<List<String>> sort
        )
        {
            var sortQuery = createSortQuery(sort);

            try {

                pageQuery =
                    page.isEmpty() && size.isEmpty() ?
                        new UnpagedSorted(createSortQuery(sort))
                        :
                        PageRequest.of(
                                page.orElse(defaultPage),
                                size.orElse(defaultPageSize),
                                sortQuery
                        );

                return this;
            }

            catch(RuntimeException exception)
            {
                throw new UseCasesException("Incorrect pagination parameters");
            }
        }

        private Sort createSortQuery(Optional<List<String>> sort)
        {
            try
            {
                return
                        sort.map(value ->
                                Sort.by(
                                        value
                                                .stream()
                                                .map(v -> Arrays.stream(v.split(",")))
                                                .flatMap(v -> v)
                                                .map(v -> v.split(":"))
                                                .map(v ->
                                                        new Sort.Order(
                                                                v.length > 1 ?
                                                                        Sort.Direction.valueOf(v[1].toUpperCase()) :
                                                                        Sort.Direction.ASC,
                                                                v[0]
                                                        )
                                                )
                                                .toList()
                                )
                        ).orElseGet(Sort::unsorted);
            }

            catch(RuntimeException exception)
            {
                throw new UseCasesException("Incorrect sorting parameters");
            }
        }
    }
}
