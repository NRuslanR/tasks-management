package edu.examples.todos.domain.actors;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ToDoTestsUtils
{
    public static List<ToDo> createSimpleTestToDos(String... names)
    {
        return Stream.of(names).map(ToDoTestsUtils::createSimpleTestToDo).toList();
    }

    public static ToDo createSimpleTestToDo(String name)
    {
        return ToDo.of(ToDoId.of(UUID.randomUUID()), name, LocalDateTime.now());
    }
}
