package edu.examples.todos.usecases.todos.common.data.generating;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ToDoInfoGeneratingUtils
{
    public static List<String> generateRandomToDoNames(int... nameCount)
    {
        return
                IntStream
                        .range(0, nameCount.length == 0 ? 1 : nameCount[0])
                        .boxed()
                        .map(v -> generateRandomToDoName())
                        .toList();
    }

    public static String generateRandomToDoName()
    {
        return generateRandomToDoId();
    }

    public static String generateRandomToDoId()
    {
        return UUID.randomUUID().toString();
    }
}
