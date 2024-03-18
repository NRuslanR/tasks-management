package edu.examples.todos.domain.operations.creation.todos;

import reactor.core.publisher.Mono;

public interface ToDoCreationService
{
    CreateToDoReply createToDo(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException;

    Mono<CreateToDoReply> createToDoAsync(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException;
}
