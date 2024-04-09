package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.ToDoPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateToDoRequest
{
    private String name;
    private String description;
    private Optional<ToDoPriority> priority;

    public static CreateToDoRequest of(String name)
    {
        return of(name, "");
    }

    private static CreateToDoRequest of(String name, String description)
    {
        return new CreateToDoRequest(name, description, Optional.empty());
    }

    private static CreateToDoRequest of(String name, String description, @NonNull ToDoPriority priority)
    {
        return new CreateToDoRequest(name, description, Optional.of(priority));
    }
}
