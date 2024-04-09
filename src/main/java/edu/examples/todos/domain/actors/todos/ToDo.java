package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "todos")
public class ToDo extends BaseEntity<ToDoId>
{
    public static ToDo of(ToDoId id, String name, String description, ToDoPriority priority, LocalDateTime createdAt)
    {
        return new ToDo(id, name, description, priority, createdAt);
    }

    public static ToDo of(ToDoId id, String name, ToDoPriority priority, LocalDateTime createdAt)
    {
        return new ToDo(id, name, priority, createdAt);
    }

    public ToDo(ToDoId id, String name, String description, ToDoPriority priority, LocalDateTime createdAt) throws ToDoNameInCorrectException
    {
        this(id, name, priority, createdAt);

        setDescription(description);
    }

    public ToDo(ToDoId id, String name, ToDoPriority priority, LocalDateTime createdAt)
    {
        super(id);

        setName(name);
        setPriority(priority);
        setCreatedAt(createdAt);
    }

    protected ToDo()
    {

    }

    private String name;

    public void setName(String newName) throws ToDoNameInCorrectException
    {
        if (!StringUtils.hasText(newName))
        {
            throw new ToDoNameInCorrectException();
        }

        name = newName;
    }

    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "type",
                    column = @Column(name = "priority_type")
            ),
            @AttributeOverride(
                    name = "value",
                    column = @Column(name = "priority_value")
            )
    })
    private ToDoPriority priority;

    public void changePriorityType(ToDoPriorityType type)
    {
        priority = priority.changeType(type);
    }

    public void changePriorityValue(int value)
    {
        priority = priority.changeValue(value);
    }

    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private LocalDateTime createdAt;

    private void setCreatedAt(@NonNull LocalDateTime value)
    {
        createdAt = adjustDate(value);
    }

    @PrePersist
    public void prePersist()
    {
        adjustDates();
    }

    @PreUpdate
    public void preUpdate()
    {
        adjustDates();
    }

    private void adjustDates()
    {
        createdAt = adjustDate(createdAt);
    }

    private LocalDateTime adjustDate(LocalDateTime value)
    {
        return value.withNano(0);
    }

    /* refactor: */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "value",
                    column = @Column(name = "parentToDoId")
            )
    })
    /*
        In most use-cases sub-todos aren't required so in order to fall not into
        N + 1 problem and for performance this solution is used. Lazy makes To-Do model and tracing more complex
        and EntityGraph fetches redundant data that's question is why are sub-todos necessary if just
        current To-Do's name is required to change. This DDD approach
     */
    private ToDoId parentToDoId;
}
