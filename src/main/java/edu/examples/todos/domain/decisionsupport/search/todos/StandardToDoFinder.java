package edu.examples.todos.domain.decisionsupport.search.todos;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoList;
import edu.examples.todos.domain.common.exceptions.DomainException;
import edu.examples.todos.persistence.repositories.todos.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandardToDoFinder implements ToDoFinder
{
    private final ToDoRepository toDoRepository;

    @Override
    public Optional<ToDo> findToDoByName(String name) throws NullPointerException, DomainException
    {
        return findToDoByNameAsync(name).blockOptional();
    }

    @Override
    public Mono<ToDo> findToDoByNameAsync(String name) throws NullPointerException, DomainException
    {
        return Mono.fromCallable(() -> toDoRepository.findByName(name).orElse(null));
    }

    @Override
    public Mono<ToDoList> findAllSubToDosRecursivelyForAsync(ToDo targetToDo)
    {
        return
                Mono
                    .fromCallable(
                            () -> toDoRepository.findAllSubToDosRecursivelyFor(targetToDo.getId().getValue())
                    )
                    .map(ToDoList::of);
    }
}
