package edu.examples.todos.presentation.api.todos.common.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoFullInfoResource extends RepresentationModel<ToDoFullInfoResource>
{
    @JsonProperty("toDo")
    private ToDoResource toDoResource;

    @JsonProperty("subTodos")
    private List<ToDoFullInfoResource> subToDoResources;
}
