package edu.examples.todos.usecases.todos.accounting.queries.getbyid;

import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetToDoByIdResult
{
    private ToDoDto toDo;
}
