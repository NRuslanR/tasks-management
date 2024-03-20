package edu.examples.todos.presentation.web.api.todos.accounting.resources;

import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.Arrays;

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
