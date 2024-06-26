package edu.examples.todos.usecases.todos.accounting.commands.update;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/* refactor: wrap optional fields by Intention generics and extend JsonMapper to convert string to Intention */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateToDoCommand
{
    private String toDoId;
    private String name;
    private String description;
    private String priorityType;
    private Optional<Integer> priorityValue;
}
