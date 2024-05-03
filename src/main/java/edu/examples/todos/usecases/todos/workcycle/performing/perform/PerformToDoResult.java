package edu.examples.todos.usecases.todos.workcycle.performing.perform;

import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformToDoResult
{
    private ToDoDto toDo;
}
