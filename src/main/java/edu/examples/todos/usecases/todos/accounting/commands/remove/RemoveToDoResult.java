package edu.examples.todos.usecases.todos.accounting.commands.remove;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveToDoResult
{
    private ToDoDto toDo;
}
