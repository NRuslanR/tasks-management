package edu.examples.todos.usecases.todos.common.behaviour.states;

import edu.examples.todos.common.config.profiles.EnabledIfTestsProfile;
import edu.examples.todos.domain.actors.todos.ToDoState;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.todos.workcycle.performing.ToDoPerformingCommandUseCases;
import edu.examples.todos.usecases.todos.workcycle.performing.ToDoPerformingCommandUseCasesTestsUtils;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@EnabledIfTestsProfile
@Component
@RequiredArgsConstructor
public class StandardToDoStateUtilService implements ToDoStateUtilService
{
    private final ToDoPerformingCommandUseCases toDoPerformingCommandUseCases;

    @Override
    public Collection<String> getIncorrectToDoStatesForUpdate()
    {
        return List.of(ToDoState.PERFORMED.toString());
    }

    @Override
    public Collection<String> getIncorrectToDoStatesForRemove()
    {
        return List.of("");
    }

    @Override
    public Collection<Pair<String, String>> getIncorrectToDoStatesForParentAssigning()
    {
        return List.of(
                Pair.with(ToDoState.PERFORMED.toString(), ""),
                Pair.with("", ToDoState.PERFORMED.toString()),
                Pair.with(ToDoState.PERFORMED.toString(), ToDoState.PERFORMED.toString())
        );
    }

    @Override
    public Collection<String> getIncorrectToDoStatesForPerform()
    {
        return List.of(ToDoState.PERFORMED.toString());
    }

    @Override
    public void setToDoState(ToDoDto toDo, String newStateId)
    {
        var todoState = ToDoState.valueOf(newStateId.toUpperCase());

        if (todoState.equals(ToDoState.PERFORMED))
        {
            var command =
                ToDoPerformingCommandUseCasesTestsUtils
                    .createCommandForToDoPerforming(toDo.getId());

            toDoPerformingCommandUseCases.performToDo(command).block();
        }
    }
}
