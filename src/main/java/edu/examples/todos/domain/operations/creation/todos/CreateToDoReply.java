package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateToDoReply
{
    private ToDo toDo;

    public static CreateToDoReply of(ToDo toDo)
    {
        return new CreateToDoReply(toDo);
    }
}
