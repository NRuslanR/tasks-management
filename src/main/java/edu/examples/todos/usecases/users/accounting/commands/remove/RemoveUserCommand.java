package edu.examples.todos.usecases.users.accounting.commands.remove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveUserCommand
{
    private String userId;
}
