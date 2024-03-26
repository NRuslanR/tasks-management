package edu.examples.todos.usecases.todos.accounting.commands.update;

import edu.examples.todos.domain.actors.todos.ToDo;

public interface UpdateToDoCommandResultMapper
{
    ToDo changeToDoByCommand(ToDo toDo, UpdateToDoCommand command)
            throws NullPointerException, IncorrectUpdateToDoCommandException;

    UpdateToDoResult toUpdateToDoResult(ToDo toDo);
}
