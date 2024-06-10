package edu.examples.todos.presentation.api.features.todos.common.resources.web;

import edu.examples.todos.presentation.api.features.todos.accounting.web.HttpApiToDoAccountingController;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoFullInfoResource;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoFullInfoResourceAssembler;
import edu.examples.todos.presentation.api.features.todos.common.resources.ToDoResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class HttpToDoFullInfoResourceAssembler extends ToDoFullInfoResourceAssembler
{
    public HttpToDoFullInfoResourceAssembler(ToDoResourceAssembler toDoResourceAssembler)
    {
        super(toDoResourceAssembler, HttpApiToDoAccountingController.class, ToDoFullInfoResource.class);
    }
}
