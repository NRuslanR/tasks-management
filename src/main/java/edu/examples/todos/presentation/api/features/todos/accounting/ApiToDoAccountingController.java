package edu.examples.todos.presentation.api.features.todos.accounting;

import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoFullInfoResource;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResource;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import org.springframework.hateoas.PagedModel;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ApiToDoAccountingController
{
    Mono<PagedModel<ToDoResource>> findToDos(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<List<String>> sort
    );

    Mono<ToDoResource> getToDoById(String toDoId);

    Mono<ToDoFullInfoResource> getToDoFullInfoById(String toDoId);

    Mono<ToDoResource> createToDo(CreateToDoCommand createToDoCommand);

    Mono<ToDoResource> updateToDo(String toDoId, UpdateToDoCommand updateToDoCommand);

    Mono<Void> removeToDo(String toDoId);
}
