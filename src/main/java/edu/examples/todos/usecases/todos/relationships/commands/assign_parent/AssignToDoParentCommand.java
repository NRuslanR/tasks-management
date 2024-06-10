package edu.examples.todos.usecases.todos.relationships.commands.assign_parent;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AssignToDoParentCommand
{
    @NotBlank
    private String targetToDoId;

    @NotBlank
    private String parentToDoId;
}
