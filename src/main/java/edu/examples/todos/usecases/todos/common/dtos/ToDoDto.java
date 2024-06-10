package edu.examples.todos.usecases.todos.common.dtos;

import edu.examples.todos.domain.actors.todos.ToDoActionsAvailability;
import edu.examples.todos.usecases.users.accounting.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
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

    private UserDto author;

    public ToDoDto combineWithActionsAvailabilityByPairwiseAnd(ToDoActionsAvailability value)
    {
        return ToDoDto.of(
                getId(),
                getName(),
                getDescription(),
                getPriorityType(),
                getPriorityValue(),
                getCreatedAt(),
                getPerformedAt(),
                getParentToDoId(),
                getActionsAvailability().pairwiseAnd(value),
                getState(),
                getDisplayState(),
                getAuthor()
        );
    }
}
