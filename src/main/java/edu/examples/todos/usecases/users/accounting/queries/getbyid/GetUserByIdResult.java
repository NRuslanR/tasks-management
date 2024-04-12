package edu.examples.todos.usecases.users.accounting.queries.getbyid;

import edu.examples.todos.usecases.users.accounting.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByIdResult
{
    private UserDto user;
}
