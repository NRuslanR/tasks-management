package edu.examples.todos.domain.actors.todos;

import lombok.Value;

@Value(staticConstructor = "of")
public class ToDoActionsAvailability
{
    boolean viewingAvailable;
    boolean changingAvailable;
    boolean removingAvailable;
    boolean parentAssigningAvailable;
    boolean performingAvailable;

    public boolean anyActionAvailable()
    {
        return
                viewingAvailable ||
                changingAvailable ||
                removingAvailable ||
                parentAssigningAvailable ||
                performingAvailable;
    }
}
