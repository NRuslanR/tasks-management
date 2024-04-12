package edu.examples.todos.usecases.users.accounting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
