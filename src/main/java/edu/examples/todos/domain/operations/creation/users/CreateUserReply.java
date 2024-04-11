package edu.examples.todos.domain.operations.creation.users;

import edu.examples.todos.domain.resources.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
@AllArgsConstructor
public class CreateUserReply
{
    private User user;
}
