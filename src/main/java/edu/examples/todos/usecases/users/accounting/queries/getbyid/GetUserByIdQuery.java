package edu.examples.todos.usecases.users.accounting.queries.getbyid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByIdQuery
{
    private String userId;
}
