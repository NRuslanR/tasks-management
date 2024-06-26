package edu.examples.todos.usecases.todos.relationships.commands;

import edu.examples.todos.usecases.common.config.IntegrationUseCasesTest;
import edu.examples.todos.usecases.todos.common.behaviour.states.ToDoStateUtilService;
import edu.examples.todos.usecases.todos.common.data.generating.ToDoCreationUtilService;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationUseCasesTest
public class IntegrationToDoRelationshipsCommandUseCasesTests extends ToDoRelationshipsCommandUseCasesTests
{
    @Autowired
    public IntegrationToDoRelationshipsCommandUseCasesTests(
            ToDoCreationUtilService toDoCreationUtilService,
            ToDoStateUtilService toDoStateUtilService,
            ToDoRelationshipsCommandUseCases toDoRelationshipsCommandUseCases
    )
    {
        super(toDoCreationUtilService, toDoStateUtilService, toDoRelationshipsCommandUseCases);
    }
}
