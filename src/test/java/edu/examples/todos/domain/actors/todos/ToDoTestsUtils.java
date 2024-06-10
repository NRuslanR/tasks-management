package edu.examples.todos.domain.actors.todos;

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
        return ToDo.of(ToDoId.of(UUID.randomUUID()), name, ToDoPriority.defaultPriority(), LocalDateTime.now(), null);
    }
}
