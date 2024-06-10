package edu.examples.todos.usecases.todos.workcycle.performing.perform;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformToDoCommand
{
    @NotBlank
    private String toDoId;
}
