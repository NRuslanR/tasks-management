package edu.examples.todos.domain.operations.creation.todos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateToDoRequest
{
    private String name;
    private String description;

    public static CreateToDoRequest of(String name)
    {
        return of(name, "");
    }

    private static CreateToDoRequest of(String name, String description)
    {
        return new CreateToDoRequest(name, description);
    }
}
