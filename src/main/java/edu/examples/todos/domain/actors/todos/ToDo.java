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
    public static ToDo of(ToDoId id, String name, String description, LocalDateTime createdAt)
    {
        return new ToDo(id, name, description, createdAt);
    }

    public static ToDo of(ToDoId id, String name, LocalDateTime createdAt)
    {
        return new ToDo(id, name, createdAt);
    }

    public ToDo(ToDoId id, String name, String description, LocalDateTime createdAt) throws ToDoNameInCorrectException
    {
        this(id, name, createdAt);

        setDescription(description);
    }

    public ToDo(ToDoId id, String name, LocalDateTime createdAt)
    {
        super(id);

        setName(name);
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
