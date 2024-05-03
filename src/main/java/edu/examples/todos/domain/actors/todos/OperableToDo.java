package edu.examples.todos.domain.actors.todos;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Data
@RequiredArgsConstructor(staticName = "of")
public class OperableToDo
{
    @NonNull
    @Delegate
    private final ToDo target;

    @NonNull
    private final ToDoActionsAvailability actionsAvailability;

    public void ensureViewingAvailable()
    {
        if (!actionsAvailability.isViewingAvailable())
            throw new ToDoActionIsNotAvailableException();
    }

    public void ensureChangingAvailable()
    {
        if (!actionsAvailability.isChangingAvailable())
            throw new ToDoActionIsNotAvailableException();
    }

    public void ensureRemovingAvailable()
    {
        if (!actionsAvailability.isRemovingAvailable())
            throw new ToDoActionIsNotAvailableException();
    }

    public void ensureParentAssigningAvailable()
    {
        if (!actionsAvailability.isParentAssigningAvailable())
            throw new ToDoActionIsNotAvailableException();
    }

    public void ensurePerformingAvailable()
    {
        if (!actionsAvailability.isPerformingAvailable())
            throw new ToDoActionIsNotAvailableException();
    }
}
