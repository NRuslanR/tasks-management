package edu.examples.todos.usecases.todos.accounting.commands.create;

import edu.examples.todos.usecases.todos.accounting.dtos.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateToDoResult
{
    private ToDoDto toDo;
}
