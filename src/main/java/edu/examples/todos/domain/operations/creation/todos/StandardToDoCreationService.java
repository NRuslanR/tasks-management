package edu.examples.todos.domain.operations.creation.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.actors.todos.ToDoNameInCorrectException;
import edu.examples.todos.domain.decisionsupport.search.todos.ToDoFinder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StandardToDoCreationService implements ToDoCreationService
{
    private final ToDoFinder toDoFinder;

    @Override
    public CreateToDoReply createToDo(@NonNull CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException
    {
        ensureRequestIsValid(request);

        var toDo = doCreateToDo(request);

        return CreateToDoReply.of(toDo);
    }

    private void ensureRequestIsValid(CreateToDoRequest request)
    {
    }

    private ToDo doCreateToDo(CreateToDoRequest request) throws IncorrectCreateToDoRequestException
    {
        ensureToDoWithSpecifiedNameDoesNotExists(request.getName());

        var toDoId = new ToDoId(UUID.randomUUID());

        var createdAt = LocalDateTime.now();

        try
        {
            var toDo = new ToDo(toDoId, request.getName(), request.getDescription(), createdAt);

            return toDo;
        }

        catch (ToDoNameInCorrectException exception)
        {
            throw new IncorrectCreateToDoRequestException(exception.getMessage());
        }
    }

    private void ensureToDoWithSpecifiedNameDoesNotExists(String name) throws ToDoAlreadyExistsDomainException
    {
        var toDo = toDoFinder.findToDoByName(name);

        if (toDo.isPresent())
        {
            throw new ToDoAlreadyExistsDomainException(name);
        }
    }
}
