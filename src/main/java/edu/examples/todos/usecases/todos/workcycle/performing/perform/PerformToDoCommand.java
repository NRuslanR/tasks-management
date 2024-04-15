package edu.examples.todos.usecases.todos.workcycle.performing.perform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformToDoCommand
{
    private String toDoId;
}
