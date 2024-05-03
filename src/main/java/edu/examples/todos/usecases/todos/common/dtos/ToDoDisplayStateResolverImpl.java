package edu.examples.todos.usecases.todos.common.dtos;

import edu.examples.todos.domain.actors.todos.ToDoState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ToDoDisplayStateResolverImpl implements ToDoDisplayStateResolver
{
    @Override
    public String resolveDisplayState(String state) throws IllegalArgumentException
    {
        var displayState =
            StringUtils.equalsIgnoreCase(state, ToDoState.CREATED.toString()) ? "Создана" :
            StringUtils.equalsIgnoreCase(state, ToDoState.PERFORMED.toString()) ? "Выполнена" : "";

        if (StringUtils.isBlank(displayState))
            throw new IllegalArgumentException("Incorrect status to figure out display status");

        return displayState;
    }
}
