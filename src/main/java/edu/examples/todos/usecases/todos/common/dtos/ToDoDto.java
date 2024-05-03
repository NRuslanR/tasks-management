package edu.examples.todos.usecases.todos.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Relation(itemRelation = "todo", collectionRelation = "todos") /* to deal with correct collection name with relation to PagedModel */
public class ToDoDto
{
    private String id;
    private String name;
    private String description;
    private String priorityType;
    private int priorityValue;
    private LocalDateTime createdAt;
    private LocalDateTime performedAt;
    private String parentToDoId;
    private ToDoActionsAvailabilityDto actionsAvailability;

    public void setParentToDoId(String value)
    {
        parentToDoId = Optional.ofNullable(value).orElse("");
    }

    private String state;
    private String displayState;
}