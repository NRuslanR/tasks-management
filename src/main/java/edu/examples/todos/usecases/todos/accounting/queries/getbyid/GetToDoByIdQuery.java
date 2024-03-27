package edu.examples.todos.usecases.todos.accounting.queries.getbyid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetToDoByIdQuery
{
    private String toDoId;
}
