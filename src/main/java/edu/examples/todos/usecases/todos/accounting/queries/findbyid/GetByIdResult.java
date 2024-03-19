package edu.examples.todos.usecases.todos.accounting.queries.findbyid;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetByIdResult
{
    private ToDoDto toDo;
}
