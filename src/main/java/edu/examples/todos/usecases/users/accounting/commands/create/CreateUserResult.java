package edu.examples.todos.usecases.users.accounting.commands.create;

import edu.examples.todos.usecases.users.accounting.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class CreateUserResult
{
    private UserDto user;

    public CreateUserResult(UserDto user)
    {
        this.user = user;
    }
}
