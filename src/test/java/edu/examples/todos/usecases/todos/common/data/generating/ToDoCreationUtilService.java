package edu.examples.todos.usecases.todos.common.data.generating;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;

import java.util.UUID;

public interface ToDoCreationUtilService
{
    default ToDoDto createRandomToDo()
    {
        return createToDo(UUID.randomUUID().toString());
    }

    ToDoDto createToDo(String toDoName);
}
