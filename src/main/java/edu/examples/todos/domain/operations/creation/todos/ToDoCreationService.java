package edu.examples.todos.domain.operations.creation.todos;

import reactor.core.publisher.Mono;

/* refactor: to turn "throws" to corresponding comment because the exceptions will be wrapped by Mono as well */
public interface ToDoCreationService
{
    CreateToDoReply createToDo(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException;

    Mono<CreateToDoReply> createToDoAsync(CreateToDoRequest request)
            throws NullPointerException, IncorrectCreateToDoRequestException, ToDoAlreadyExistsDomainException;
}
