package edu.examples.todos.usecases.todos.accounting.commands.create;

import edu.examples.todos.domain.operations.creation.todos.CreateToDoReply;
import edu.examples.todos.domain.operations.creation.todos.CreateToDoRequest;

public interface CreateToDoCommandResultMapper
{
    CreateToDoRequest toCreateToDoRequest(CreateToDoCommand createToDoCommand);

    CreateToDoResult toCreateToDoResult(CreateToDoReply createToDoReply);
}
