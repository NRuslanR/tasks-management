package edu.examples.todos.domain.operations.creation.todos;

public interface ToDoCreationService
{
    CreateToDoReply createToDo(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException;
}
