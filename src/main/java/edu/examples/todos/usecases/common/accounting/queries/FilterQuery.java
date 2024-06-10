package edu.examples.todos.usecases.common.accounting.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterQuery
{
    private Map<String, Object> fields = new HashMap<>();

    public static FilterQuery empty()
    {
        return new FilterQuery();
    }

    public boolean isEmpty()
    {
        return fields.isEmpty();
    }
}
