package edu.examples.todos.usecases.todos.accounting.queries.findtodos;

import edu.examples.todos.usecases.common.accounting.queries.FindObjectsQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindToDosQuery
{
    @Delegate
    private FindObjectsQuery internalQuery;
}
