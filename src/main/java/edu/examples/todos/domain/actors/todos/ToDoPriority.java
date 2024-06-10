package edu.examples.todos.domain.actors.todos;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.EnumUtils;

import java.util.Objects;

@Value
@Accessors(fluent = true)
@Embeddable
public class ToDoPriority
{
    @Enumerated(EnumType.STRING)
    ToDoPriorityType type;
    int value;

    static ToDoPriority DEFAULT = ToDoPriority.of(ToDoPriorityType.MEDIUM, 0);

    public static ToDoPriority defaultPriority()
    {
        return DEFAULT;
    }

    public static ToDoPriority of(ToDoPriorityType type, int value)
    {
        return new ToDoPriority(type, value);
    }

    public static ToDoPriority of(String priorityTypeName, int value)
    {
        return new ToDoPriority(priorityTypeName, value);
    }

    private ToDoPriority(String typeName, int value)
    {
        this(resolvePriorityType(typeName), value);
    }

    private ToDoPriority(ToDoPriorityType type, int value)
    {
        this.type = Objects.requireNonNull(type);

        if (value < 0)
            throw new IncorrectToDoPriorityValueException("To-do's priority value can't be negative");

        this.value = value;
    }

    private static ToDoPriorityType resolvePriorityType(String typeName)
    {
        var realTypeName = typeName.toUpperCase();

        if(EnumUtils.isValidEnum(ToDoPriorityType.class, realTypeName))
            return ToDoPriorityType.valueOf(realTypeName);

        throw new IncorrectToDoPriorityTypeException();
    }

    protected ToDoPriority()
    {
        type = ToDoPriorityType.LOW;
        value = 0;
    }

    public ToDoPriority changeType(ToDoPriorityType type)
    {
        return ToDoPriority.of(type, value);
    }

    public ToDoPriority changeType(String typeName) throws IncorrectToDoPriorityTypeException
    {
        return ToDoPriority.of(type, value);
    }

    public ToDoPriority changeValue(int value) throws IncorrectToDoPriorityValueException
    {
        return ToDoPriority.of(type, value);
    }
}
