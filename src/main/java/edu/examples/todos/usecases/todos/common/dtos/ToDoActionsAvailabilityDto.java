package edu.examples.todos.usecases.todos.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ToDoActionsAvailabilityDto
{
    private boolean viewingAvailable;
    private boolean changingAvailable;
    private boolean removingAvailable;
    private boolean parentAssigningAvailable;
    private boolean performingAvailable;

    public static ToDoActionsAvailabilityDto onlyViewingAvailable()
    {
        return new ToDoActionsAvailabilityDto(true, false, false, false, false);
    }
}
