package edu.examples.todos.usecases.todos.common.behaviour.states;

import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import org.javatuples.Pair;

import java.util.Collection;

public interface ToDoStateUtilService
{
    Collection<String> getIncorrectToDoStatesForUpdate();
    Collection<String> getIncorrectToDoStatesForRemove();
    Collection<Pair<String, String>> getIncorrectToDoStatesForParentAssigning();
    Collection<String> getIncorrectToDoStatesForPerform();

    void setToDoState(ToDoDto toDo, String newStateId);
}
