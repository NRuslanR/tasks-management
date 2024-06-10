package edu.examples.todos.usecases.todos.accounting.commands.remove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveToDoCommand
{
    private String toDoId;
}
