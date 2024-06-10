package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateToDoReply
{
    private OperableToDo toDo;

    public static @NonNull CreateToDoReply of(OperableToDo toDo)
    {
        return new CreateToDoReply(toDo);
    }
}
