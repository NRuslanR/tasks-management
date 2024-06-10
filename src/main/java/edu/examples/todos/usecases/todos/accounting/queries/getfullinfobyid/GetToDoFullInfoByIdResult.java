package edu.examples.todos.usecases.todos.accounting.queries.getfullinfobyid;

import edu.examples.todos.usecases.todos.common.dtos.ToDoFullInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetToDoFullInfoByIdResult
{
    private ToDoFullInfoDto toDoFullInfo;
}
