package edu.examples.todos.usecases.todos.relationships.commands.assign_parent;

import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignToDoParentResult
{
    private ToDoDto toDo;
}
