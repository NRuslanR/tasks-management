package edu.examples.todos.usecases.users.accounting.commands.remove;

import edu.examples.todos.usecases.users.accounting.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveUserResult
{
    private UserDto user;
}
