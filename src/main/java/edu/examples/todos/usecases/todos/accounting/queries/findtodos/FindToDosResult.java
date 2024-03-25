package edu.examples.todos.usecases.todos.accounting.queries.findtodos;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindToDosResult
{
    private Page<ToDoDto> toDoPage;
}
