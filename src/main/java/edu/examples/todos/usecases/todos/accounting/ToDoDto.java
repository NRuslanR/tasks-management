package edu.examples.todos.usecases.todos.accounting;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
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
    private String status;

    @Setter(AccessLevel.NONE)
    private String displayStatus;

    private LocalDateTime performedAt;

    private String parentToDoId;

    public void setParentToDoId(String value)
    {
        parentToDoId = Optional.ofNullable(value).orElse("");
    }

    public void setStatus(String value)
    {
        status = value;

        updateDisplayStatus();
    }

    private void updateDisplayStatus()
    {
        displayStatus =
                Objects.equals(status,"created") ? "Создана" :
                        Objects.equals(status, "performed") ? "Выполнена" : "";

        if (!StringUtils.hasText(displayStatus))
            throw new IllegalStateException("Incorrect status to figure out display status");
    }
}
