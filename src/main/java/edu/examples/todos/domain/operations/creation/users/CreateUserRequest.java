package edu.examples.todos.domain.operations.creation.users;

import edu.examples.todos.domain.resources.users.UserName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NonNull
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateUserRequest
{
    private UserName name;
    private Integer allowedToDoCreationCount;
    private Boolean editForeignTodosAllowed;
    private Boolean removeForeignTodosAllowed;
    private Boolean performForeignTodosAllowed;
}
