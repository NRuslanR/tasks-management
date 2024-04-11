package edu.examples.todos.usecases.users.accounting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private String id;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private Integer allowedToDoCreationCount;
    private Boolean editForeignTodosAllowed;
    private Boolean removeForeignTodosAllowed;
    private Boolean performForeignTodosAllowed;

}
