package edu.examples.todos.usecases.todos.accounting.queries.findbyid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetByIdQuery
{
    private String toDoId;
}
