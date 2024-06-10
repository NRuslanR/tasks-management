package edu.examples.todos.usecases.todos.common.dtos;

import edu.examples.todos.domain.actors.todos.ToDoActionsAvailability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
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

    public ToDoActionsAvailabilityDto pairwiseAnd(ToDoActionsAvailability other)
    {
        return ToDoActionsAvailabilityDto.of(
                isViewingAvailable() && other.isViewingAvailable(),
                isChangingAvailable() && other.isChangingAvailable(),
                isRemovingAvailable() && other.isRemovingAvailable(),
                isParentAssigningAvailable() && other.isParentAssigningAvailable(),
                isPerformingAvailable() && other.isPerformingAvailable()
        );
    }
}
