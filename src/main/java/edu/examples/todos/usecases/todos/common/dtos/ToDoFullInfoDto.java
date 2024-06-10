package edu.examples.todos.usecases.todos.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoFullInfoDto
{
    private ToDoDto toDo;
    private List<ToDoFullInfoDto> subToDos;

    public Optional<ToDoFullInfoDto> findSubToDoById(String toDoId)
    {
        return subToDos.stream().filter(v -> v.getToDo().getId().equals(toDoId)).findFirst();
    }
}
