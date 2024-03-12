package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class StandardToDoCreationService implements ToDoCreationService
{
    @Override
    public CreateToDoReply createToDo(CreateToDoRequest request) throws NullPointerException
    {
        ensureRequestIsValid(request);

        var toDo = doCreateToDo(request);

        return CreateToDoReply.of(toDo);
    }

    private void ensureRequestIsValid(CreateToDoRequest request) throws NullPointerException
    {
        Objects.requireNonNull(request);
    }

    private ToDo doCreateToDo(CreateToDoRequest request)
    {
        var toDoId = new ToDoId(UUID.randomUUID());
        var createdAt = LocalDateTime.now();

        return new ToDo(toDoId, request.getName(), request.getDescription(), createdAt);
    }
}
