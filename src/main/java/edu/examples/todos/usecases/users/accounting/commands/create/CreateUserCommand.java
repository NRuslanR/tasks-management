package edu.examples.todos.usecases.users.accounting.commands.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand
{
    private String firstName;
    private String lastName;
    private Integer allowedToDoCreationCount;
    private Boolean editForeignTodosAllowed;
    private Boolean removeForeignTodosAllowed;
    private Boolean performForeignTodosAllowed;

    public CreateUserCommand(String firstName, String lastName)
    {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }
}
