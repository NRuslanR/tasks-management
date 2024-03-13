package edu.examples.todos.domain.decisionsupport.search.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.common.exceptions.DomainException;

import java.util.Optional;

public interface ToDoFinder
{
    Optional<ToDo> findToDoByName(String name) throws NullPointerException, DomainException;
}
