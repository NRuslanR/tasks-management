package edu.examples.todos.usecases.todos.common.data.generating;

import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;

import java.util.List;
import java.util.stream.Stream;

import static edu.examples.todos.usecases.todos.common.data.generating.ToDoInfoGeneratingUtils.generateRandomToDoId;

public interface ToDoCreationUtilService
{
    default List<ToDoDto> createRandomToDos(int... count)
    {
        return
            count.length == 0 ? List.of(createRandomToDo()):
            ToDoInfoGeneratingUtils
                    .generateRandomToDoNames(count)
                    .stream()
                    .map(this::createToDo)
                    .toList();
    }

    default List<ToDoDto> createToDos(String... names)
    {
        return
                Stream
                    .of(names)
                    .map(this::createToDo)
                    .toList();
    }

    default ToDoDto createRandomToDo()
    {
        return createToDo(generateRandomToDoId());
    }

    ToDoDto createToDo(String toDoName);
}
