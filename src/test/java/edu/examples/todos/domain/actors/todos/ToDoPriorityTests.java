package edu.examples.todos.domain.actors.todos;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToDoPriorityTests
{
    @Test
    public void should_Be_ValidToDoPriority_When_TypeAndValueValid()
    {
        var type = ToDoPriorityType.MEDIUM;
        var value = 14;

        var priority = ToDoPriority.of(type, value);

        assertEquals(type, priority.type());
        assertEquals(value, priority.value());
    }

    @Test
    public void should_ThrowException_When_PriorityType_Is_Null()
    {
        assertThrows(NullPointerException.class, () -> {

            ToDoPriority.of((ToDoPriorityType) null, 0);

        });
    }

    @Test
    public void should_ThrowException_When_PriorityValue_Is_Negative()
    {
        assertThrows(IncorrectToDoPriorityValueException.class, () -> {

            ToDoPriority.of(ToDoPriorityType.MEDIUM, -1);

        });
    }
}
