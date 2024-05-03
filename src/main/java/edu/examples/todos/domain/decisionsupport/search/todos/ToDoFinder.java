package edu.examples.todos.domain.decisionsupport.search.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.actors.todos.ToDoList;
import edu.examples.todos.domain.common.exceptions.DomainException;
import reactor.core.publisher.Mono;

import java.util.Optional;

/* refactor: to turn "throws" to corresponding comment because the exceptions will be wrapped by Mono as well */
public interface ToDoFinder
{
    Optional<ToDo> findToDoById(ToDoId id) throws NullPointerException, DomainException;

    Mono<ToDo> findToDoByIdAsync(ToDoId id) throws NullPointerException, DomainException;

    Optional<ToDo> findToDoByName(String name) throws NullPointerException, DomainException;

    Mono<ToDo> findToDoByNameAsync(String name) throws NullPointerException, DomainException;

    Mono<ToDoList> findAllSubToDosRecursivelyForAsync(ToDo targetToDo);
}
