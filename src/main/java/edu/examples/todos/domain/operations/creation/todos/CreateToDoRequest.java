package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.ToDoPriority;
import edu.examples.todos.domain.resources.users.UserId;
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
    private UserId authorId;

    public static CreateToDoRequest of(String name, UserId authorId)
    {
        return of(name, "", authorId);
    }

    private static CreateToDoRequest of(String name, String description, UserId authorId)
    {
        return new CreateToDoRequest(name, description, Optional.empty(), authorId);
    }

    private static CreateToDoRequest of(String name, String description, @NonNull ToDoPriority priority, UserId authorId)
    {
        return new CreateToDoRequest(name, description, Optional.of(priority), authorId);
    }
}
