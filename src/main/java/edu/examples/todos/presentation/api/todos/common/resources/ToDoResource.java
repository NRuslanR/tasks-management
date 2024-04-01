package edu.examples.todos.presentation.api.todos.common.resources;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.core.Relation;

import java.util.Arrays;

@Relation(itemRelation = "todo", collectionRelation = "todos")
public class ToDoResource extends EntityModel<ToDoDto>
{
    public static ToDoResource of(ToDoDto toDoDto, Link... links)
    {
        return new ToDoResource(toDoDto, links);
    }

    public ToDoResource(ToDoDto toDoDto, Link... links)
    {
        super(toDoDto, Arrays.asList(links));
    }
}
