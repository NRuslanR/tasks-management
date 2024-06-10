package edu.examples.todos.usecases.todos.accounting.commands.remove;

import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RemoveToDoResult
{
    private ToDoDto toDo;
}
