package edu.examples.todos.usecases.todos.relationships.commands.assign_parent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignToDoParentCommand
{
    private String targetToDoId;
    private String parentToDoId;
}
