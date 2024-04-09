package edu.examples.todos.usecases.todos.accounting.commands.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateToDoCommand
{
    private String name;
    private String description;
    private String priorityType;
    private Optional<Integer> priorityValue;
}
