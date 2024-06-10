package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentCommand;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.AssignToDoParentResult;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.IncorrectAssignToDoParentCommandException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface ToDoRelationshipsCommandUseCases
{
    Mono<AssignToDoParentResult> assignToDoParent(@Valid AssignToDoParentCommand command)
            throws
            NullPointerException,
            IncorrectAssignToDoParentCommandException,
            ToDoNotFoundException,
            ToDoIsInCorrectToBeParentException;
}
