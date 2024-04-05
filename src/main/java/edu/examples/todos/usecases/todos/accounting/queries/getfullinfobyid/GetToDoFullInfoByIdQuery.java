package edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetToDoFullInfoByIdQuery
{
    private String toDoId;
}
