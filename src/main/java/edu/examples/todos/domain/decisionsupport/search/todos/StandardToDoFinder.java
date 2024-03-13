package edu.examples.todos.domain.decisionsupport.search.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.common.exceptions.DomainException;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandardToDoFinder implements ToDoFinder
{
    private final ToDoRepository toDoRepository;

    @Override
    public Optional<ToDo> findToDoByName(String name) throws NullPointerException, DomainException
    {
        return toDoRepository.findByName(name);
    }
}
